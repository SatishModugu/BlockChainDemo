import java.util.Date;
public class basicblock {

	public 	String outputHash;
	public  String inputHash;
	private String blockData; 
	private long time;
	
	public basicblock(String blockData,String inputHash )
	{
		this.blockData = blockData;
		this.inputHash = inputHash;
		this.time = new Date().getTime();
		this.outputHash = hashCalculater();
	}
	
	public String hashCalculater() {
		String hashSHA256 = hashing.hashSha256(inputHash +Long.toString(time) +blockData );
		return hashSHA256;
	}
}
