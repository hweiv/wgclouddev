package com.wgcloud.util.license;

import cn.hutool.core.codec.Base64;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSAEncrypt {
   public static RSAPublicKey loadPublicKeyByStr(String publicKeyStr) throws Exception {
      try {
         byte[] buffer = Base64.decode(publicKeyStr);
         KeyFactory keyFactory = KeyFactory.getInstance("RSA");
         X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
         return (RSAPublicKey)keyFactory.generatePublic(keySpec);
      } catch (NoSuchAlgorithmException var4) {
         throw new Exception("无此算法");
      } catch (InvalidKeySpecException var5) {
         throw new Exception("公钥非法");
      } catch (NullPointerException var6) {
         throw new Exception("公钥数据为空");
      }
   }

   public static byte[] decrypt(RSAPublicKey publicKey, byte[] cipherData) throws Exception {
      if (publicKey == null) {
         throw new Exception("解密公钥为空, 请设置");
      } else {
         Cipher cipher = null;

         try {
            cipher = Cipher.getInstance("RSA");
            cipher.init(2, publicKey);
            byte[] output = cipher.doFinal(cipherData);
            return output;
         } catch (NoSuchAlgorithmException var4) {
            throw new Exception("无此解密算法");
         } catch (NoSuchPaddingException var5) {
            var5.printStackTrace();
            return null;
         } catch (InvalidKeyException var6) {
            throw new Exception("解密公钥非法,请检查");
         } catch (IllegalBlockSizeException var7) {
            throw new Exception("密文长度非法");
         } catch (BadPaddingException var8) {
            throw new Exception("密文数据已损坏");
         }
      }
   }
}
