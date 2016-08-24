/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Object;

/**
 *
 * @author GEN13
 */
public class Event
{

    private String id;
    private String ownerId;
    private double lat, lon;
    private String title;
    private String snippet;
    private boolean draggable, visible;
    private int nombreRodeurs;
    private byte[] imageByte;
    private String iconName;
    private int res;
    int type;
    
    public Event()
    {
    }

    public Event(String id, double lat, double lon, String title, String snippet,int nombreRodeurs)
    {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.title = title;
        this.snippet = snippet;
        this.nombreRodeurs = nombreRodeurs;
        //this.imageByte = imageByte;
        
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    
    public int getRes()
    {
        return res;
    }

    public void setRes(int res)
    {
        this.res = res;
    }

   

    public String getOwnerId()
    {
        return ownerId;
    }

    public void setOwnerId(String ownerId)
    {
        this.ownerId = ownerId;
    }

    public String getIconName()
    {
        return iconName;
    }

    public void setIconName(String iconName)
    {
        this.iconName = iconName;
    }

    
    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public int getNombreRodeurs()
    {
        return nombreRodeurs;
    }

    public void setNombreRodeurs(int nombreRodeurs)
    {
        this.nombreRodeurs = nombreRodeurs;
    }

    public byte[] getImageByte()
    {
        return imageByte;
    }

    public void setImageByte(byte[] imageByte)
    {
        this.imageByte = imageByte;
    }
    
    
    public double getLat()
    {
        return lat;
    }

    public void setLat(double lat)
    {
        this.lat = lat;
    }

    public double getLon()
    {
        return lon;
    }

    public void setLon(double lon)
    {
        this.lon = lon;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getSnippet()
    {
        return snippet;
    }

    public void setSnippet(String snippet)
    {
        this.snippet = snippet;
    }

    @Override
    public String toString()
    {
        return title + "#" + snippet + "#" + lat + "#" + lon + "#" + ownerId + "#" + res + "#" + type;
    }
    
    public void stringToObject(String[] data)
    {
        this.title = data[2];
        this.snippet = data[3];
        this.lat = Double.parseDouble(data[4]);
        this.lon = Double.parseDouble(data[5]);
        this.ownerId = data[6];
        this.res = Integer.parseInt(data[7]);
        this.type = Integer.parseInt(data[8]);
    }
    

}