using System;
using System.IO;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;

namespace az_key_vault
{
    class Program
    {
        static void Main(string[] args)
        {
            Console.WriteLine("Azure Key Vault demo Started!");

            Console.WriteLine();
            Console.WriteLine("*********** Using DI ************");
            var builder = new ConfigurationBuilder().SetBasePath(Directory.GetCurrentDirectory()).AddJsonFile("appsettings.json", optional: true, reloadOnChange: true);
            var diContainer = new ServiceCollection();
            diContainer.AddSingleton<ISecurityVault, AzureKeyVault>();
            diContainer.AddSingleton<IConfigurationRoot>(builder.Build());
            diContainer.AddSingleton<SomeClass, SomeClass>(); // Added to show constructor injection

            var diProvider = diContainer.BuildServiceProvider();
            var securityVault = diProvider.GetService<ISecurityVault>(); 
            var someObject = diProvider.GetService<SomeClass>(); // The above will often be injected in the constructor
            //var secret = securityVault.GetSecret("my-secret");            
            //Console.WriteLine($"secret={secret}");
            //createSecrets(securityVault);

            var rsaPair2PubKey = securityVault.GetSecret("rsa-pair2-pub-key");
            Console.WriteLine($"rsaPair2PubKey={rsaPair2PubKey}");

            Console.WriteLine();
            Console.WriteLine("*********** Not using DI ************");
            var rsaPrivateKeyNonDI = new AzureKeyVault(builder.Build()).GetSecret("rsa-pair1-prv-key");
            Console.WriteLine($"key={rsaPrivateKeyNonDI}");

            Console.WriteLine("Azure Key Vault demo Ended!");
        }

        private static void createSecrets(ISecurityVault securityVault)
        {
            var secret = File.ReadAllText($"..\\local\\rsa-pair1-prv-key.key");
            securityVault.CreateSecret("rsa-pair1-prv-key", secret);
            secret = File.ReadAllText($"..\\local\\rsa-pair1-pub-key.key");
            securityVault.CreateSecret("rsa-pair1-pub-key", secret);
            //secret = File.ReadAllText($"..\\local\\rsa-pair2-prv-key.key");
            //securityVault.CreateSecret("rsa-pair2-prv-key", secret);
            secret = File.ReadAllText($"..\\local\\rsa-pair2-pub-key.key");
            securityVault.CreateSecret("rsa-pair2-pub-key", secret);
        }
    }
}
