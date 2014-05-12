package whyq.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import android.util.Base64;
import android.util.Log;

public class RSA {

	private KeyPairGenerator kpg;
	private Cipher cipher;
	private KeyPair keyPair;
	private PrivateKey privKey;
	private PublicKey pubKey;

	// "-----BEGIN PUBLIC KEY-----
	public static String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDQXdqfSG3anZ/oBgfBCErLZiaNyv+N1FjnSyKTP0q37/ihP/50n/HpCHrEoQPv4tvxZZb8w069bbxtMlb9mwGqjzWohcpfNle+dtch3AEliZ2jhPfT750Wr9zlk6q3/uGlcLaj10cL4R0FLBbTiEhLePqblDb/L4dD3Ck6qEhqJwIDAQAB";
	public static String PRIVATE_KEY = "MIICXQIBAAKBgQDQXdqfSG3anZ/oBgfBCErLZiaNyv+N1FjnSyKTP0q37/ihP/50n/HpCHrEoQPv4tvxZZb8w069bbxtMlb9mwGqjzWohcpfNle+dtch3AEliZ2jhPfT750Wr9zlk6q3/uGlcLaj10cL4R0FLBbTiEhLePqblDb/L4dD3Ck6qEhqJwIDAQABAoGBAMBxvoaYjaV5KYRRdX3qW7IqQXd6QrdKpWXR9jgLH8Zso7TCdxBd3T6+by0GDa3UWBFHI8GF3UaFgJ9V/BQ0wr4rYLmi5P1y8B1jiT7caHXJE46g1vhxkXiTrg6uxoK7oBGdvZd81a9cJ6Dl/Trz15+2odglIs+veqGj0pbInc9pAkEBvbOg3xo1Ykmdj6OacFeq+tRqEugzQ3n7bS2U8uPTmERFwu92poRxoTwbpdkA10rh9CS0q0tbLSBL80Bca1ithQJAd649UKjyVcFQLSX1n2g399nM8pkGtlsNVR9KWR4ldfWy9MEreuEYdG/lAToeSosguMUbqsyvxSJVY8sR0H4iuwJBAaLBzckwb63cKqyVc3pP9DZdMxh2kAu65U8L/6FCC0FKDB1+LgV/9N8fgX6OZ6rgEXH4tsMmNxEsAhAXjX3SQJ0CQGzssTN7QGvaMF2XAdEeamny2bwmmFxZzG2ft+waKuAMSd7G/QquWbKHsHaIbc9MhvCDvOWbu5IA6JSV2tXWJ0cCQQFb5Qgs/ZOWjoiZvN4qjpPSHCOnXWC8vgC96jVHHLm9CpB26xlf7HBMXJsbyFZ7qXs6lNLT61g/VKGBaVG6zgr";
	KeyPair kp;
	PublicKey publicKey;
	PrivateKey privateKey;
	byte[] encryptedBytes, decryptedBytes;
	Cipher cipher1;
	String encrypted, decrypted;

	public RSA() {

	}


	public String RSAEncrypt(final String pin) throws NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		String result = null;

		if(pin!=null){
			if(!pin.equals("")){
				byte[] sigBytes2 = Base64.decode(PUBLIC_KEY, Base64.DEFAULT);
				Log.d("WS", "new key is: " + PUBLIC_KEY);
				try {
					    byte[] encodedKey = sigBytes2;
					    X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedKey);
					    KeyFactory kf = KeyFactory.getInstance("RSA");
					    PublicKey pkPublic = kf.generatePublic(publicKeySpec);
					    // Encrypt
					    Cipher pkCipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
					    pkCipher.init(Cipher.ENCRYPT_MODE, pkPublic);
					    encryptedBytes = pkCipher.doFinal(pin.getBytes("UTF-8"));
					    result = Base64.encodeToString(encryptedBytes, Base64.DEFAULT);
//					    RSADecrypt(result);
					    result.replaceFirst(" ", "");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	public String RSADecrypt(final String pin)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		String result = null;
		byte[] sigBytes2 = Base64.decode(PUBLIC_KEY, Base64.DEFAULT);
		Log.d("WS", "new key is: " + PUBLIC_KEY);
		try {
			    byte[] encodedKey = sigBytes2;
			    X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedKey);
			    KeyFactory kf = KeyFactory.getInstance("RSA");
			    PublicKey pkPublic = kf.generatePublic(publicKeySpec);
			    // Encrypt
			    // decrypts the message
			    byte[] dectyptedText = null;
			    Cipher cipher = Cipher.getInstance("RSA");
			    cipher.init(Cipher.DECRYPT_MODE, pkPublic);
			    dectyptedText = cipher.doFinal(Base64.decode(pin,  Base64.DEFAULT));
			    result = Base64.encodeToString(dectyptedText, Base64.DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
