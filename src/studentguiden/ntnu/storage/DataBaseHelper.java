package studentguiden.ntnu.storage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import studentguiden.ntnu.misc.Util;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;



public class DataBaseHelper extends SQLiteOpenHelper{
 
    //The Android's default system path of your application database.
	//TODO: kanskje dette skal være "data/datastudentguiden/ntnu/storage/databases/" ?
    private static String DB_PATH = "/data/data/studentguiden/ntnu/storage/databases/";
 
    private static String DB_NAME = "studassen";
 
	private static final String KEY_CODE = "code";
	private static final String KEY_NAME = "name"; 
	private static final String MYCOURSES_TABLE = "mycourses";
	private static final String LECTURES_TABLE = "lectures";
	private static final String DATABASE_TABLE = "courses";
	
    private SQLiteDatabase myDataBase; 
 
    private final Context myContext;
 
    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DataBaseHelper(Context context) {
 
    	super(context, DB_NAME, null, 1);
        this.myContext = context;
    }	
 
  /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException{
 
    	boolean dbExist = checkDataBase();
 
    	if(dbExist){
    		//do nothing - database already exist
    	}else{
 
    		//By calling this method and empty database will be created into the default system path
               //of your application so we are gonna be able to overwrite that database with our database.
        	this.getReadableDatabase();
 
        	try {
 
    			copyDataBase();
 
    		} catch (IOException e) {
 
        		throw new Error("Error copying database");
 
        	}
    	}
 
    }
 
    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){
 
    	SQLiteDatabase checkDB = null;
 
    	try{
    		String myPath = DB_PATH + DB_NAME;
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
 
    	}catch(SQLiteException e){
 
    		//database does't exist yet.
 
    	}
 
    	if(checkDB != null){
 
    		checkDB.close();
 
    	}
 
    	return checkDB != null ? true : false;
    }
 
    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{
 
    	//Open your local db as the input stream
    	InputStream myInput = myContext.getAssets().open(DB_NAME);
 
    	// Path to the just created empty db
    	String outFileName = DB_PATH + DB_NAME;
 
    	//Open the empty db as the output stream
    	OutputStream myOutput = new FileOutputStream(outFileName);
 
    	//transfer bytes from the inputfile to the outputfile
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
 
    	//Close the streams
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
 
    }
 
    public void openDataBase() throws SQLException{
 
    	//Open the database
        String myPath = DB_PATH + DB_NAME;
    	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
 
    }
 
    @Override
	public synchronized void close() {
 
    	    if(myDataBase != null)
    		    myDataBase.close();
 
    	    super.close();
 
	}
 
	@Override
	public void onCreate(SQLiteDatabase db) {
 
	}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
 
	}
 
	public long insertMyCourse(String code, String name) {
		Util.log("inserting course to my courses: "+code);
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_CODE, code);
		initialValues.put("name", name);
		return myDataBase.insert(MYCOURSES_TABLE,null, initialValues);
	}
	
	public long insertLecture(String code, String start, String end, String day, int dayNumber, String week, String room) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_CODE, code);
		initialValues.put("start", start);
		initialValues.put("end", end);
		initialValues.put("day", day);
		initialValues.put("dayNumber", dayNumber);
		initialValues.put("weeks", week);
		initialValues.put("room", room);
		return myDataBase.insert(LECTURES_TABLE, null, initialValues);
	}

	//---deletes a particular title---
	public boolean deleteCourse(String rowId) {
		return myDataBase.delete(DATABASE_TABLE, KEY_CODE + 
				"=" + rowId, null) > 0;
	}

	public Cursor getAllCourses() {
		return myDataBase.query(DATABASE_TABLE, new String[] {
				KEY_CODE,
				KEY_NAME},
				null,
				null,
				"code",
				null,
				null);
	}
	
	public Cursor getMyLectures() {
		return myDataBase.query(LECTURES_TABLE, new String[] {
				"_id",
				KEY_CODE,
				"start",
				"end",
				"day",
				"dayNumber",
				"weeks",
				"room"
		}, null, null, null, null, "start");
	}
	
	public Cursor getMyCourses() {
		return myDataBase.query(MYCOURSES_TABLE, new String[] {
				KEY_CODE,
				"name"
		}, null, null, null, null, KEY_CODE);
	}


	//---retrieves a particular title---
	public Cursor getCourse(String code) throws SQLException {
		Cursor mCursor =
			myDataBase.query(true, DATABASE_TABLE, new String[] {
					KEY_CODE, 
					KEY_NAME
			}, 
			KEY_CODE + "=" + code, 
			null,
			null, 
			null, 
			null, 
			null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	//---updates a title---
	public boolean updateCourse(String code, String name) {
		ContentValues args = new ContentValues();
		args.put(KEY_CODE, code);
		args.put(KEY_NAME, name);
		return myDataBase.update(DATABASE_TABLE, args, 
				KEY_CODE + "=" + code, null) > 0;
	}

}