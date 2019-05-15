import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.util.Base64;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        Sender sender = new Sender();       // Sender
        Receiver receiver = new Receiver(); // Receiver

        // Session key 교환 과정(RSA public/private key 이용)
        String sessionKey;
        sessionKey = sender.sendSessionKey(receiver.getPublicKey()); // Sender: session key를 generate해서 receiver의 public key로 encrypt한 뒤 send(보내는 과정은 우선 생략)
        receiver.receiveSessionKey(sessionKey);                      // Receiver: sender로부터 받은 session key를 자신의 private key로 decrypt

        // Session key를 통한 암,복호화
        try {
            String text = "Hello my name is jungmin";
            String encrypted = CryptoManager.encrypt(Algorithm.AES, text, sender.getSessionKey());        // Sender의 session key로 message encrypt
            String decrypted = CryptoManager.decrypt(Algorithm.AES, encrypted, receiver.getSessionKey()); // Receiver의 session key로 이를 decrypt

            boolean result = text.equals(decrypted);
            System.out.println(result ? "Success!" : "Fail!");
            System.out.println("--------------------------------------------------");
            System.out.println("Text encrypted by Sender: " + text);
            System.out.println("Text decrypted by Receiver: " + decrypted);
        } catch (NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
    }
}

/*
---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
Algorithm definition
---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
 */
enum Algorithm {
    RSA("RSA", 2048),
    AES("AES", 128);

    private String name;
    private int keySize;

    Algorithm(String name, int keySize) {
        this.name = name;
        this.keySize = keySize;
    }

    public String getName() {
        return name;
    }

    public int getKeySize() {
        return keySize;
    }
}

/*
---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
Users
---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
 */
class User {
    private PublicKey publicKey;   // 공개키
    private PrivateKey privateKey; // 개인키
    private Key sessionKey;        // 세션키(Symmetric)

    public User() {
        initKeys();
    }

    protected void initKeys() {
        KeyPair keyPair;
        try {
            // RSA algorithm 이용한 public, private key 생성
            keyPair = KeyManager.getNewKeyPair(Algorithm.RSA);
            this.publicKey = keyPair.getPublic();
            this.privateKey = keyPair.getPrivate();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        this.sessionKey = null;
    }

    // Getter, setter
    public Key getSessionKey() {
        return sessionKey;
    }

    protected void setSessionKey(Key sessionKey) {
        this.sessionKey = sessionKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    protected PrivateKey getPrivateKey() {
        return privateKey;
    }
}

class Sender extends User {
    @Override
    protected void initKeys() {
        setSessionKey(generateSessionKey()); // Sender는 session key 생성
    }

    private Key generateSessionKey() {
        Key sessionKey = null;

        try {
            sessionKey = KeyManager.getNewKey(Algorithm.AES);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return sessionKey;
    }

    // 생성한 session key를 receiver의 public key를 이용해 암호화, 보내는 과정은 생략
    String sendSessionKey(Key publicKeyOfReceiver) {
        String encrypted = CryptoManager.encryptKey(Algorithm.RSA, getSessionKey(), publicKeyOfReceiver); // RSA를 사용한 키 암호화
        return encrypted;
    }
}

class Receiver extends User {
    // 받은 key를 자신의 private key를 통해 복호화 후 session key로 setting
    void receiveSessionKey(String received) {
        Key decrypted = CryptoManager.decryptKey(Algorithm.RSA, received, this.getPrivateKey()); // RSA를 사용한 키 복호화
        setSessionKey(decrypted);
    }
}

/*
---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
Managers
---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
 */
class KeyManager {
    public static Key getNewKey(Algorithm algorithm) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator;
        keyGenerator = KeyGenerator.getInstance(algorithm.getName());
        keyGenerator.init(algorithm.getKeySize());
        return keyGenerator.generateKey();
    }

    public static KeyPair getNewKeyPair(Algorithm algorithm) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator;
        keyPairGenerator = KeyPairGenerator.getInstance(algorithm.getName());
        keyPairGenerator.initialize(algorithm.getKeySize());
        return keyPairGenerator.generateKeyPair();
    }

    public static byte[] encodeKey(Key key) {
        return key.getEncoded();
    }

    public static Key decodeKey(byte[] encoded) {
        return new SecretKeySpec(encoded, 0, encoded.length, Algorithm.AES.getName());
    }
}

class CryptoManager {
    private static Cipher getCipher(int mode, String algorithm, Key key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(mode, key);
        return cipher;
    }

    // Encrypt, decrypt texts
    static String encrypt(Algorithm algorithm, String plainText, Key key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = getCipher(Cipher.ENCRYPT_MODE, algorithm.getName(), key);
        byte[] bytes = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(bytes);
    }

    static String decrypt(Algorithm algorithm, String cipherText, Key key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = getCipher(Cipher.DECRYPT_MODE, algorithm.getName(), key);
        byte[] bytes = Base64.getDecoder().decode(cipherText);
        return new String(cipher.doFinal(bytes));
    }

    // Encrypt, decrypt keys
    static String encryptKey(Algorithm algorithm, Key sessionKey, Key publicKey) {
        String ret = null;
        try {
            Cipher cipher = getCipher(Cipher.ENCRYPT_MODE, algorithm.getName(), publicKey);
            byte[] bytes = cipher.doFinal(KeyManager.encodeKey(sessionKey));
            ret = Base64.getEncoder().encodeToString(bytes);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return ret;
    }

    static Key decryptKey(Algorithm algorithm, String sessionKey, Key privateKey) {
        Key ret = null;
        try {
            Cipher cipher = getCipher(Cipher.DECRYPT_MODE, algorithm.getName(), privateKey);
            byte[] bytes = Base64.getDecoder().decode(sessionKey);
            ret = KeyManager.decodeKey(cipher.doFinal(bytes));
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException e) {
            e.printStackTrace();
        }
        return ret;
    }
}