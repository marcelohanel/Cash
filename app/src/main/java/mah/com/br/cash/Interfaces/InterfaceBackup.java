package mah.com.br.cash.Interfaces;

import java.io.File;

/**
 * Created by CGI - Marcelo on 15/05/2015.
 */
public interface InterfaceBackup {
    void onFinishBackup(File s);

    void onFinishRestore(String s);
}
