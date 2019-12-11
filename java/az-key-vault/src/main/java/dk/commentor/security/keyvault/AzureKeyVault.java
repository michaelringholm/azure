package dk.commentor.security.keyvault;

import java.util.Properties;

import com.microsoft.azure.AzureEnvironment;
import com.microsoft.azure.credentials.AppServiceMSICredentials;
import com.microsoft.azure.keyvault.KeyVaultClient;
import com.microsoft.azure.keyvault.models.KeyBundle;
import com.microsoft.azure.keyvault.models.SecretBundle;
import com.microsoft.azure.keyvault.requests.UpdateSecretRequest;

import dk.commentor.sl.ISecurityVault;

// https://docs.microsoft.com/en-us/azure/key-vault/key-vault-developers-guide
// https://docs.microsoft.com/en-us/azure/key-vault/service-to-service-authentication
//
// Use az login when running locally and MI (Managed Identity) in Azure
public class AzureKeyVault implements ISecurityVault {
    private KeyVaultClient _keyVault;
    private String _keyVaultUrl;

    public AzureKeyVault(Properties properties) throws Exception {
        AppServiceMSICredentials credentials = new AppServiceMSICredentials(AzureEnvironment.AZURE);
        _keyVault = new KeyVaultClient(credentials);
        //keyVaultClient.getSecret("https://xxxx.vault.azure.net","secretName");

        //var tokenProvider = new AzureServiceTokenProvider();
        //_keyVault = new KeyVaultClient(new KeyVaultClient.AuthenticationCallback(tokenProvider.KeyVaultTokenCallback));
        _keyVaultUrl = properties.getProperty("key-vault:url");
        if (_keyVaultUrl == null || _keyVaultUrl.isEmpty())
            throw new Exception("Please add the url of the key vault in appsettings.json file as key-vault with property url.");
    }

    public String GetKey(String keyName) {
        KeyBundle key = _keyVault.getKey(_keyVaultUrl, keyName);
        return key.key().toString();
    }

    public String GetSecret(String secretName) {
        SecretBundle secret = _keyVault.getSecret(_keyVaultUrl, secretName);
        return secret.value();
    }

    public void CreateSecret(String secretName, String secret) {
        /*UpdateSecretRequest updateSecretRequest = new UpdateSecretRequest();
        updateSecretRequest.
        _keyVault.updateSecret(_keyVaultUrl, secretName, secret);*/
    }
}
