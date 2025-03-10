package com;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;
public class AES {
	static byte[] keydata; 
	public static SecretKeySpec generateKey(byte data[])throws Exception{
		SecretKeySpec key = new SecretKeySpec(data,0,data.length,"AES");
		keydata = key.getEncoded();
		return key;
	}

	public static byte[] encrypt(byte input[],byte keys[])throws Exception{
		SecretKeySpec key = generateKey(keys);
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key);
	    byte encbytes[] = cipher.doFinal(input);
		return encbytes;
	}
	public static byte[] decrypt(byte[] enc,byte keys[])throws Exception {
		SecretKeySpec key = generateKey(keys);
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte decbytes[] = cipher.doFinal(enc);
		return decbytes;
	}
	/*public static void main(String args[])throws Exception{
		String key = "123456rootkeysda";
		byte enc_key[] = generateKey(key.getBytes()).getEncoded();
		byte enc[] = encrypt("kaleem".getBytes(),enc_key);
		byte dec[] = decrypt(enc,enc_key);
		System.out.println(new String(enc)+" "+new String(dec));
	}*/
}         