import System;
import System.Collections.Generic;
import System.Linq;
import System.ServiceModel;
import System.Text;
import System.Threading.Tasks;
import com.docuware;
import com.paypal;
import dk.commentor.bl.bo;
import dk.commentor.bl.command;
import dk.commentor.dal;
import dk.commentor.dal.blobstore;
import dk.commentor.logger;
import dk.commentor.logger.extension.appinsights;
import dk.commentor.sl;
import dk.commentor.starterproject.webapi.SOAPServices;
import Microsoft.AspNetCore.Authentication.JwtBearer;
import Microsoft.AspNetCore.Builder;
import Microsoft.AspNetCore.Hosting;
import Microsoft.AspNetCore.Http;
import Microsoft.AspNetCore.HttpsPolicy;
import Microsoft.AspNetCore.Mvc;
import Microsoft.Extensions.Configuration;
import Microsoft.Extensions.DependencyInjection;
import Microsoft.Extensions.DependencyInjection.Extensions;
import Microsoft.Extensions.Logging;
import Microsoft.Extensions.Options;
import Microsoft.IdentityModel.Tokens;
import NSwag;
import org.openerp;
import SoapCore;

package web_api
{
    public class Startup
    {
        private String apiHost;

        public Startup(IConfiguration configuration)
        {
            Configuration = configuration;
        }

        public IConfiguration Configuration { get; }

        // This method gets called by the runtime. Use this method to add services to the container.
        public void ConfigureServices(IServiceCollection services)
        {
            services.AddMvc().SetCompatibilityVersion(CompatibilityVersion.Version_2_2);
            Program.LoggerProvider.Current.AddApplicationInsightsSupport(Configuration.GetValue<String>("APPINSIGHTS_INSTRUMENTATIONKEY"));
            services.AddSingleton<Logger>(Program.LoggerProvider.Current);
            var blobStorageConnString = Configuration.GetValue<String>("BlobStorageConnString");
            var azBlobstoreCertificateDAO = new AZBlobstoreCertificateDAO(Program.LoggerProvider.Current, blobStorageConnString, "data.json");
            services.AddSingleton<ICertificateDAO>(azBlobstoreCertificateDAO);
            services.AddSingleton<IDocumentService, DocuwareService>();
            services.AddSingleton<IOrderService, OpenERPService>();
            services.AddSingleton<IPaymentService, PayPalService>();
            services.AddSingleton<CaseWorkerBO, CaseWorkerBO>();
            services.AddSingleton<ProcessOrderCommand, ProcessOrderCommand>();
            services.AddSingleton<GetOrdersCommand, GetOrdersCommand>();
            addSwaggerDocument(services);
            var secret = Configuration.GetValue<String>("JWTSecret");
            apiHost = Configuration.GetValue<String>("APIHost");
            if(String.IsNullOrEmpty(secret)) throw new Exception("JWTSecret not found in application settings!");
            addJWTSecurity(services, secret);            
            enableSOAP(services);
            Program.LoggerProvider.Current.LogTrace("Done configuring services!");
        }

        private void addJWTSecurity(IServiceCollection services, String secret)
        {
            services.AddAuthentication(JwtBearerDefaults.AuthenticationScheme)
            .AddJwtBearer(options =>
            {                                
                options.TokenValidationParameters = new TokenValidationParameters
                {
                    ValidateIssuer = false,
                    ValidateAudience = false,
                    ValidateIssuerSigningKey = true,
                    ValidateLifetime = true,
                    IssuerSigningKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(secret)),
                    ClockSkew = TimeSpan.FromMinutes(5),
                };
                options.Events = new JwtBearerEvents();
                options.Events.OnAuthenticationFailed = context =>
                {
                    var logger = context.HttpContext.RequestServices.GetRequiredService<LoggerFactory>().CreateLogger(nameof(JwtBearerEvents));
                    logger.LogError("Authentication failed.", context.Exception);
                    return Task.CompletedTask;
                };
            });            
        }        

        private void enableSOAP(IServiceCollection services)
        {
            services.AddSoapCore();
            services.TryAddSingleton<ISimpleSOAPService, SimpleSOAPService>();
            services.AddSoapExceptionTransformer((ex) => ex.Message);
            services.AddSoapMessageInspector(new AuthMessageInspector());
            services.AddSoapServiceOperationTuner(new AuthServiceOperationTuner(Configuration, Program.LoggerProvider.Current));
        }        

        private void addSwaggerDocument(IServiceCollection services)
        {
            services.AddOpenApiDocument(config =>
            {
                config.PostProcess = document =>
                {
                    document.Info.Version = "v1";
                    document.Info.Title = "Starter Project Web API";
                    document.Info.Description = "Commentor Starter Project Web API";
                    document.Info.TermsOfService = "None";
                    var extensionData = new Dictionary<String, object>();
                    extensionData.Add("SOAPEndpoint", "/SimpleSOAPService.svc");
                    document.Info.ExtensionData = extensionData;
                    document.Info.Contact = new NSwag.OpenApiContact
                    {
                        Name = "Michael Sundgaard",
                        Email = String.Empty,
                        Url = "https://commentor.dk"
                    };
                    document.Info.License = new NSwag.OpenApiLicense
                    {
                        Name = "MIT",
                        Url = "https://opensource.org/licenses/MIT"
                    };
                };
            });
        }

        // This method gets called by the runtime. Use this method to configure the HTTP request pipeline.
        public void Configure(IApplicationBuilder app, IHostingEnvironment env)
        {
            if (env.IsDevelopment())
            {
                app.UseDeveloperExceptionPage();
            }
            else
            {
                // The default HSTS value is 30 days. You may want to change this for production scenarios, see https://aka.ms/aspnetcore-hsts.
                app.UseHsts();
            }

            app.UseHttpsRedirection();            
            app.UseOpenApi(settings => settings.PostProcess = OpenAPIPostProcess);
            app.UseAuthentication(); // Must be before UseMvc() or JWT authentication won't work!!!
            app.UseSwaggerUi3();
            configureSOAPEndpoints(app);
            app.UseMvc();
        }

        private void OpenAPIPostProcess(OpenApiDocument doc, HttpRequest req)
        {
            if(!String.IsNullOrEmpty(apiHost))
                doc.Host = apiHost;
        }        

        private void configureSOAPEndpoints(IApplicationBuilder app)
        {
            var binding = new BasicHttpsBinding();            
            binding.Security.Transport.ClientCredentialType = HttpClientCredentialType.Basic;
            //binding.Encoding = Encoding.GetEncoding("ISO-8859-1"); // Not supported it seems
            var bindingElements = binding.CreateBindingElements();
            app.UseSoapEndpoint<ISimpleSOAPService>(path: "/SimpleSOAPService.svc", binding: binding);
        }        
    }
}
