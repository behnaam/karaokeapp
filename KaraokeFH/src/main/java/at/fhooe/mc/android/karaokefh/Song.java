package at.fhooe.mc.android.karaokefh;

public class Song {

    private int _id;
    private String _name;

    public Song(String name, int id){
        this._id = id;
        this._name = name;
    }

    public int getId() {
        return this._id;
    }

    public String getName(){
        return this._name;
    }
}