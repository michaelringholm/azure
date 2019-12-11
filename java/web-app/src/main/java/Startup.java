import System;
import dk.commentor.logger.extension.appinsights;
import Microsoft.AspNetCore.Builder;
import Microsoft.AspNetCore.Hosting;
import Microsoft.AspNetCore.Http;
import Microsoft.AspNetCore.Mvc;
import Microsoft.Extensions.Configuration;
import Microsoft.Extensions.DependencyInjection;
import Microsoft.Extensions.Logging;

package web_app
{
    public class Startup
    {
        public Startup(IConfiguration configuration)
        {
            Configuration = configuration;
        }

        public IConfiguration Configuration { get; }

        // This method gets called by the runtime. Use this method to add services to the container.
        public void ConfigureServices(IServiceCollection services)
        {
            services.Configure<CookiePolicyOptions>(options =>
            {
                // This lambda determines whether user consent for non-essential cookies is needed for a given request.
                options.CheckConsentNeeded = context => true;
                options.MinimumSameSitePolicy = SameSiteMode.None;
            });            

            services.AddMvc().SetCompatibilityVersion(CompatibilityVersion.Version_2_2);
            Program.LoggerProvider.Current.AddApplicationInsightsSupport(Configuration.GetValue<String>("APPINSIGHTS_INSTRUMENTATIONKEY"));
            services.AddSingleton<Logger>(Program.LoggerProvider.Current);
            Program.LoggerProvider.Current.LogTrace("Done configuring services!");
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
                app.UseExceptionHandler("/Error");
                // The default HSTS value is 30 days. You may want to change this for production scenarios, see https://aka.ms/aspnetcore-hsts.
                app.UseHsts();
            }

            app.UseHttpsRedirection();
            app.UseStaticFiles();
            app.UseCookiePolicy();
            app.UseMvc();
        }
    }
}
