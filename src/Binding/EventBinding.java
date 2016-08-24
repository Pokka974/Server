/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Binding;

import Object.Event;
import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;

/**
 *
 * @author GEN13
 */
public class EventBinding extends TupleBinding
{

    @Override
    public Object entryToObject(TupleInput input)
    {
          
                String id = input.readString();
                String owner = input.readString();
                int rodeurs = input.readInt();

                double lat = input.readDouble();
                double lon = input.readDouble();

                String title = input.readString();
                String snippet = input.readString();
                
                int res = input.readInt();
                int type = input.readInt();
                
       
       Event e = new Event();
       
                e.setId(id);
                e.setOwnerId(owner);
                e.setNombreRodeurs(rodeurs);
                e.setLat(lat);
                e.setLon(lon);
                e.setTitle(title);
                e.setSnippet(snippet);
                e.setRes(res);
                e.setType(type);
                
        return e;
    }

    @Override
    public void objectToEntry(Object o, TupleOutput output)
    {
       Event e = (Event) o;
       
       output.writeString(e.getId());
       output.writeString(e.getOwnerId());
       output.writeInt(e.getNombreRodeurs());
       output.writeDouble(e.getLat());
       output.writeDouble(e.getLon());
       output.writeString(e.getTitle());
       output.writeString(e.getSnippet());
       output.writeInt(e.getRes());
       output.writeInt(e.getType());
       
       //output.writeFast(e.getImageByte());
    }
    
}
