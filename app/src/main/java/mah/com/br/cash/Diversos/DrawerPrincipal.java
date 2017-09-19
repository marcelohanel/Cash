package mah.com.br.cash.Diversos;

public class DrawerPrincipal {

    private String title;
    private int icon;

    public DrawerPrincipal() {
    }

    public DrawerPrincipal(String title, int icon) {
        this.title = title;
        this.icon = icon;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return this.icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}