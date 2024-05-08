package roomescape.member.encoder;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.spec.KeySpec;
import java.util.Base64;

public class Pbkdf2PasswordEncoder implements PasswordEncoder {

    private final byte[] salt;
    private final int iterationCount;
    private final int keyLength;

    public Pbkdf2PasswordEncoder(
            final byte[] salt,
            final int iterationCount,
            final int keyLength
    ) {
        this.salt = salt;
        this.iterationCount = iterationCount;
        this.keyLength = keyLength;
    }

    @Override
    public String encode(final String password) {
        try {
            final KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
            final SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            final byte[] hash = factory.generateSecret(spec).getEncoded();

            return Base64.getEncoder().encodeToString(hash);
        } catch (final Exception e) {
            throw new IllegalStateException("비밀번호 암호화에 실패하였습니다.");
        }
    }

    @Override
    public boolean matches(final String plainPassword, final String encodedPassword) {
        try {
            final KeySpec spec = new PBEKeySpec(plainPassword.toCharArray(), salt, iterationCount, keyLength);
            final SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            final byte[] hash = factory.generateSecret(spec).getEncoded();
            final String newHash = Base64.getEncoder().encodeToString(hash);

            return newHash.equals(encodedPassword);
        } catch (final Exception e) {
            throw new IllegalStateException("로그인 비밀번호 검증에 실패하였습니다.");
        }
    }
}
