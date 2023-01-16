package com.example.vendasta.ShoeStore.configuration;

//Copyright 2017 - https://github.com/lbalmaceda
//Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
//The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
//THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.pkcs.RSAPrivateKey;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import java.io.IOException;
import java.io.Reader;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;

public class PemUtils {


    private static byte[] parsePEMFile(Reader pemFile) throws IOException {

        PemReader reader = new PemReader(pemFile);
        PemObject pemObject = reader.readPemObject();
        byte[] content = pemObject.getContent();
        reader.close();
        return content;
    }

    private static PrivateKey getPKCS1PrivateKey(byte[] keyBytes, String algorithm) {
        PrivateKey privateKey = null;
        try {
            RSAPrivateKey asn1PrivKey = RSAPrivateKey.getInstance(ASN1Sequence.fromByteArray(keyBytes));
            RSAPrivateKeySpec rsaPrivateKeySpec = new RSAPrivateKeySpec(asn1PrivKey.getModulus(), asn1PrivKey.getPrivateExponent());
            KeyFactory kf = KeyFactory.getInstance(algorithm);
            privateKey = kf.generatePrivate(rsaPrivateKeySpec);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Could not reconstruct the private key, the given algorithm could not be found.");
        } catch (InvalidKeySpecException e) {
            System.out.println("Could not reconstruct the private key");
        } catch (IOException e) {
            System.out.println("error");
            e.printStackTrace();
        }

        return privateKey;
    }

    public static PrivateKey readPKCS1PrivateKeyFromFile(Reader reader, String algorithm) throws IOException {
        byte[] bytes = PemUtils.parsePEMFile(reader);
        return PemUtils.getPKCS1PrivateKey(bytes, algorithm);
    }
}