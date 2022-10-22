package ec.com.xprl.efactura;
import org.jetbrains.annotations.NotNull;
import xades4j.providers.impl.KeyStoreKeyingDataProvider;

import java.security.cert.X509Certificate;

/**
 * A simple password provider that directly returns a configured password string.
 */
class DirectPasswordProvider implements KeyStoreKeyingDataProvider.KeyStorePasswordProvider,
        KeyStoreKeyingDataProvider.KeyEntryPasswordProvider
{
    private final char[] password;

    /**
     * Constructor.
     *
     * @param password the password value returned by this provider.
     */
    public DirectPasswordProvider(@NotNull String password)
    {
        this.password = password.toCharArray();
    }

    /**
     * Get the password value as a KeyStore password.
     *
     * @return the value of the password stored in this provider.
     */
    @Override
    public char[] getPassword()
    {
        return password;
    }

    /**
     * Get the password value as a KeyEntry password.
     *
     * @param alias the alias for the entry to which this password will be applied.
     * @param certificate
     *
     * @return the value of the password stored in this provider.
     */
    @Override
    public char[] getPassword(String alias, X509Certificate certificate)
    {
        return password;
    }
}
