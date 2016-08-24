/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Binding;

import Object.User;
import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;

/**
 *
 * @author GEN13
 */
public class UserBinding extends TupleBinding
{
    @Override
    public Object entryToObject(TupleInput input)
    {
                String id = input.readString();
                String nickname = input.readString();
                String email = input.readString();
                String password = input.readString();
                String situation = input.readString();
                String emailVisible = input.readString();
                String newFriendId = input.readString();
                
                int icon = input.readInt();
                int age = input.readInt();
                int sexe = input.readInt();
                
        
        User u = new User();      
        
                u.setId(id);
                u.setNickname(nickname);
                u.setEmail(email);
                u.setPassword(password);
                u.setSituation(situation);
                u.setEmailVisible(emailVisible);
                u.setNewFriendId(newFriendId);
                
                u.setIconId(icon);
                u.setAge(age);
                u.setSexe(sexe);
                
        
        return u;
    }

    @Override
    public void objectToEntry(Object o, TupleOutput output)
    {
        User u = (User) o;
        
        output.writeString(u.getId());
        output.writeString(u.getNickname());
        output.writeString(u.getEmail());
        output.writeString(u.getPassword());
        output.writeString(u.getSituation());
        output.writeString(u.getEmailVisible());
        output.writeString(u.getNewFriendId());
        
        output.writeInt(u.getIconId());
        output.writeInt(u.getAge());
        output.writeInt(u.getSexe());
        
       // output.writeFast(u.getAvatar());
    }
    
}
