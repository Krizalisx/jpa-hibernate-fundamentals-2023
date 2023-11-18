package com.tutorial.generators;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Base64;
import java.util.UUID;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class UUIDGenerator implements IdentifierGenerator {

    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {
        try {
            return sign(UUID.randomUUID().toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String sign(String primaryKeyValue) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        KeyPairGenerator kg = KeyPairGenerator.getInstance("RSA");
        kg.initialize(2048);
        KeyPair keyPair = kg.generateKeyPair();
        PrivateKey aPrivate = keyPair.getPrivate();

        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(aPrivate);
        signature.update(primaryKeyValue.getBytes());
        byte[] result = signature.sign();

        return primaryKeyValue + "." + Base64.getEncoder().encodeToString(result);
    }
}
