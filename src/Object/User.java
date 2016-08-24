/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Object;

import java.util.ArrayList;

/**
 *
 * @author GEN13
 */
public class User
{
    String id;
    String password;
    String nickname;
    String email;
    String emailVisible;
    int iconId;
    int age;
    int sexe; //1 = homme
    String situation;
    boolean log;
    byte[] avatar;
    
    ArrayList<String> friendList = new ArrayList<>();
    String newFriendId;
    
    public User()
    {
    }
    
    public User(String id, String nickname, String email, String password, int age, int sexe, String situation)
    {
        this.id = id;
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.age = age;
        this.sexe = sexe;
        this.situation = situation;
        
    }

    public User(String nickname, String email, String password)
    {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }

    public int getIconId()
    {
        return iconId;
    }

    public void setIconId(int iconId)
    {
        this.iconId = iconId;
    }

    public ArrayList<String> getFriendList()
    {
        return friendList;
    }

    public void setFriendList(ArrayList<String> friendList)
    {
        this.friendList = friendList;
    }

    public String getNewFriendId()
    {
        return newFriendId;
    }

    public void setNewFriendId(String newFriendId)
    {
        this.newFriendId = newFriendId;
    }

    public void addFriend(String id)
    {
        this.friendList.add(id);
    }
    

    public String getEmailVisible()
    {
        return emailVisible;
    }

    public void setEmailVisible(String emailVisible)
    {
        this.emailVisible = emailVisible;
    }

    public byte[] getAvatar()
    {
        return avatar;
    }

    public void setAvatar(byte[] avatar)
    {
        this.avatar = avatar;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }
  
    public int getSexe()
    {
        return sexe;
    }

    public void setSexe(int sexe)
    {
        this.sexe = sexe;
    }

    public void setLog(boolean log)
    {
        this.log = log;
    }
    
    public boolean getLog()
    {
        return log;
    }
    
    public String getId()
    {
        return id;
    }

    public String getPassword()
    {
        return password;
    }

    public String getNickname()
    {
        return nickname;
    }

    public int getAge()
    {
        return age;
    }

    public String getSituation()
    {
        return situation;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public void setNickname(String nickname)
    {
        this.nickname = nickname;
    }
    
    public void setAge(int age)
    {
        this.age = age;
    }
    
    public void setSituation(String situation)
    {
        this.situation = situation;
    }

    @Override
    public String toString()
    {
        return nickname + "#" + email +  "#" + password + "#" + age + "#" + sexe + "#" + situation + "#" + id + "#" + emailVisible + "#" + iconId + "#" + newFriendId;
    }
    
    public void stringToObject(String[] data)
    {
        this.nickname = data[2];
        this.email = data[3];
        this.password = data[4];
        this.age = Integer.parseInt(data[5]);
        this.sexe = Integer.parseInt(data[6]);
        this.situation = data[7];
        this.id = data[8];
        this.emailVisible = data[9];
        
        if(Integer.parseInt(data[10]) != 0)
            this.iconId = Integer.parseInt(data[10]);
        
        if(data[11] != null)
            this.friendList.add(data[11]);
    }
    
}