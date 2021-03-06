import java.io.FileReader;
import java.io.BufferedReader;
import java.io.File;
import java.lang.Exception;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PackageManager {
    public static final String DEFAULT_FILENAME="./package.json";
    private final Runtime r = Runtime.getRuntime();
    private String currentFilename = "", currentDirectory="";
    PackageManager(String filename){
        this.currentFilename = filename;
    }
    PackageManager(){
        this.currentFilename = DEFAULT_FILENAME;
    }
    private String fileGetContents(String fileName) throws IOException{
        StringBuffer sb = new StringBuffer();
        String line = "";
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            bufferedReader.close();         
        }
        catch(IOException ex) {
            throw ex;                
        }
        return sb.toString();
    }

    public ArrayList<String> extractDependancy(String filename) throws Exception{
        ArrayList<String> dependacies = new ArrayList<>();
        try{
            //get the file content
            String filecontent = fileGetContents(filename);
            //make a regex for the extraction
            Pattern p = Pattern.compile("dependencies\": *\\{([^}]*?)\\}");
            Matcher m = p.matcher(filecontent);
            String depString = "";
            if(m.find()){
                depString = m.group(1);
            }
            for(String d: depString.split(",")){
                if(!d.trim().isEmpty())
                dependacies.add(d.trim().replace("\n", ""));
            }
        }catch(IOException e){
            throw e;
        }
        return dependacies;
    }

    static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }
    public int commandRunner(String command) throws Exception{
        try{
            Process p = r.exec(command);
            return p.waitFor();
        }catch(InterruptedException | IOException e){
            throw e;
        }
    }
    public ArrayList<String> makeEnvironment(){
        this.currentDirectory = System.getProperty("user.dir");
        ArrayList<String> env = new ArrayList<>();
        env.add("python -m venv .");
        // if(isWindows()){
        //     env.add(this.currentDirectory+"\\Scripts\\activate.bat");
        // }else{
        //     env.add(this.currentDirectory+"/Scripts/activate");
        // }
        return env;
    }
    public ArrayList<String> envCleanup(){
        ArrayList<String> env = new ArrayList<>();
        if(isWindows()){
            env.add(this.currentDirectory+"\\Scripts\\deactivate.bat");
        }else{
            env.add(this.currentDirectory+"/Scripts/deactivate");
        }
        return env;
    }
    public ArrayList<String> commandBuilder(ArrayList<String> deps){
        ArrayList<String> cmds = new ArrayList<>();
        String[] fixes = new String[]{
            "python -m pip install ",
            ""
        };
        for(String d: deps){
            cmds.add(fixes[0]+d+fixes[1]);
        }
        return cmds;
    }
    public boolean isEnvAlreadyCreated(){
       File f = new File("pyvenv.cfg");
       return (f.isFile() && f.exists());
    }
    public boolean isEnvActivated(){
        if(isEnvAlreadyCreated()){
            String vEnv = System.getenv("VIRTUAL_ENV");
            return vEnv!=null && !vEnv.isEmpty();
        }
        return false;
    }
    public boolean runSerialCommands(ArrayList<String> commands) throws Exception{
        boolean ret = true;
        for(String c: commands){
            int wf = commandRunner(c);
            if(wf != 0){
                System.out.println(c+" is failed");
                ret = false;
            }else{
                System.out.println(c+" is Done!");
            }
        }
        return ret;
    }
    public void start() throws Exception{
        ArrayList<String> commands = new ArrayList<>();
        //if env is there but not activated
        
        if(!isEnvAlreadyCreated()){
            commands.addAll(makeEnvironment());
            if(!runSerialCommands(commands)){
                System.out.println("Error Occured!");
                return;
            }
        }
        if(isEnvAlreadyCreated() && !isEnvActivated()){
            System.out.println("Please Activate venv by running \n.\\Scripts\\activate");
            System.out.println("Then run this program again!");
            return;
        }
        if(isEnvActivated()){
            commands.clear();
            commands.addAll(commandBuilder(
                extractDependancy(currentFilename)
            ));
            if(!runSerialCommands(commands)){
                System.out.println("Error Occured!");
            }else{
                System.out.println("\nPackages successfully installed!");
            }
        }
        
        
    }
}