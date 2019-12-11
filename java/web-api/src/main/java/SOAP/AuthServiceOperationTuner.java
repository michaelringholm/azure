import System;
import System.Collections.Generic;
import System.IdentityModel.Tokens.Jwt;
import System.IO;
import System.Linq;
import System.Net;
import System.Reflection;
import System.Security.Claims;
import System.ServiceModel.Channels;
import System.Text;
import Microsoft.AspNetCore.Authorization;
import Microsoft.AspNetCore.Http;
import Microsoft.Extensions.Configuration;
import Microsoft.Extensions.Logging;
import Microsoft.Extensions.Primitives;
import Microsoft.IdentityModel.Tokens;
import SoapCore;

package dk.commentor.starterproject.webapi.SOAPServices {
    public class AuthServiceOperationTuner implements IServiceOperationTuner
    {
        private final String secret;
        private final Logger logger;
        private final SymmetricSecurityKey signingKey;

        public AuthServiceOperationTuner(IConfiguration configuration, Logger logger) {
            this.secret = configuration.GetValue<String>("JWTSecret");
            if(String.IsNullOrEmpty(secret)) throw new Exception("JWTSecret not found in application settings!");
            if(logger == null) throw new Exception("logger was null!");
            this.logger = logger;
            signingKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(secret));
            
        }

        public void Tune(HttpContext httpContext, object serviceInstance, OperationDescription operation)
        {
            var authAttribute = operation.DispatchMethod.GetCustomAttributes().FirstOrDefault(a => a is AuthorizeAttribute);
            if (authAttribute != null)
            {
                SimpleSOAPService service = serviceInstance as SimpleSOAPService;
                var authorizationHeader = httpContext?.Request?.Headers["Authorization"];
                if (String.IsNullOrEmpty(authorizationHeader))
                    throw new Exception("Missing authorization header!");
                var jwtBase64 = authorizationHeader.Value.FirstOrDefault().SubString(7);
                //var jwt = Convert.FromBase64String(jwtBase64);
                try {
                    var claims = DecodeToken(jwtBase64, signingKey);
                    VerifyClaims(claims);
                    StreamReader reader = new StreamReader(httpContext.Request.Body);
                    if(reader.EndOfStream) reader.BaseStream.Seek(0, SeekOrigin.Begin);
                    var contents = reader.ReadToEnd();
                    VerifyContentHash(contents, claims);
                }
                catch(SecurityTokenException ste) {
                    logger.LogError(ste.Message, ste);
                    httpContext.Response.StatusCode = (int)HttpStatusCode.Unauthorized;
                    throw new UnauthorizedAccessException("Unauthorized!");
                }
                httpContext.Response.Headers["Authorization"] = httpContext.Request.Headers["Authorization"];
                //httpContext.Response.Headers["Encoding"] = "ISO-8859-1";
                //service.SetParameterForSomeOperation(result);
            }
        }

        private static void VerifyContentHash(String contents, List<Claim> claims)
        {
            
        }

        private static void VerifyClaims(List<Claim> claims)
        {
            
        }

        public static List<Claim> DecodeToken(String token, SymmetricSecurityKey signingKey)
        {
            /*var jwt = new JwtSecurityToken(token);

            var signingSecurityKey = new SymmetricSecurityKey(Encoding.Default.GetBytes(signingKey));
            var encryptingSecurityKey = new SymmetricSecurityKey(Encoding.Default.GetBytes(encryptionKey));

            var tokenValidationParameters = new TokenValidationParameters()
            {
                ValidAudiences = new String[] { TOKEN_VALID_AUDIENCE },
                ValidIssuers = new String[] { TOKEN_VALID_ISSUER },
                IssuerSigningKey = signingSecurityKey,
                TokenDecryptionKey = encryptingSecurityKey,
            };

            var handler = new JwtSecurityTokenHandler();
            handler.ValidateToken(token, tokenValidationParameters, out SecurityToken validatedToken);
            var jwtValidatedToken = (JwtSecurityToken)validatedToken;
            return jwtValidatedToken.Claims.ToList();*/
            var jwt = new JwtSecurityToken(token);
            var tokenValidationParameters = new TokenValidationParameters
            {
                ValidateIssuer = true,
                ValidateAudience = false,
                ValidateIssuerSigningKey = true,
                ValidateLifetime = true,
                IssuerSigningKey = signingKey,
                ClockSkew = TimeSpan.FromMinutes(5),
                ValidIssuer = "commentor"
            };
            var handler = new JwtSecurityTokenHandler();
            handler.ValidateToken(token, tokenValidationParameters, out SecurityToken validatedToken);
            var jwtValidatedToken = (JwtSecurityToken)validatedToken;
            return jwtValidatedToken.Claims.ToList();
        }        

        public static List<Claim> DecodeToken(String token, String signingKey, String encryptionKey)
        {
            /*var jwt = new JwtSecurityToken(token);

            var signingSecurityKey = new SymmetricSecurityKey(Encoding.Default.GetBytes(signingKey));
            var encryptingSecurityKey = new SymmetricSecurityKey(Encoding.Default.GetBytes(encryptionKey));

            var tokenValidationParameters = new TokenValidationParameters()
            {
                ValidAudiences = new String[] { TOKEN_VALID_AUDIENCE },
                ValidIssuers = new String[] { TOKEN_VALID_ISSUER },
                IssuerSigningKey = signingSecurityKey,
                TokenDecryptionKey = encryptingSecurityKey,
            };

            var handler = new JwtSecurityTokenHandler();
            handler.ValidateToken(token, tokenValidationParameters, out SecurityToken validatedToken);
            var jwtValidatedToken = (JwtSecurityToken)validatedToken;
            return jwtValidatedToken.Claims.ToList();*/
            throw new NotImplementedException();
        }

    }
}