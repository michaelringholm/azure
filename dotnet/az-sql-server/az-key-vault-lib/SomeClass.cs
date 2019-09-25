using System;

namespace az_key_vault
{
    public class SomeClass
    {
        private ISecurityVault _securityVault;
        public SomeClass(ISecurityVault securityVault) {
            _securityVault = securityVault;
            Console.WriteLine($"vaultType={_securityVault.ToString()}");
        }
    }
}