package dk.commentor.sl;

public interface ISecurityVault {
    String GetKey(String keyName);
    String GetSecret(String secretName);
    void CreateSecret(String secretName, String secret);
}
