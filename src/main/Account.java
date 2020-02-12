package main;

public class Account {

    String Email;
    String Username;
    String Password;
    boolean Trained;
    int Wealth;
    String Type;
    boolean Banned;
    boolean requestMule;

    public Account(String Email, String Username, String Password, boolean Trained, int Wealth,
                   String Type, boolean Banned, boolean requestMule) {
        this.Email = Email;
        this.Username = Username;
        this.Password = Password;
        this.Trained = Trained;
        this.Wealth = Wealth;
        this.Type = Type;
        this.Banned = Banned;
        this.requestMule = requestMule;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public boolean isTrained() {
        return Trained;
    }

    public void setTrained(boolean trained) {
        Trained = trained;
    }

    public int getWealth() {
        return Wealth;
    }

    public void setWealth(int wealth) {
        Wealth = wealth;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public boolean isBanned() {
        return Banned;
    }

    public void setBanned(boolean banned) {
        Banned = banned;
    }

    public boolean wantsMule() {
        return requestMule;
    }

    public void setMule(boolean wantsMule) {
        requestMule = wantsMule;
    }

    public String toString() {

        return ("Email: " + getEmail() + " Username: " + getUsername() + " Password: " + getPassword() + " Trained: " + isTrained() + " Value: " + getWealth() + " Type: " + getType() + " Banned: " + isBanned() + " Mule: " + wantsMule());
    }
}
