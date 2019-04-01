package basic;
import java.util.Date;
public class basicblock {

	public 	String outputHash;
	public  String inputHash;
	private String blockData; 
	private long time;
	private int uniNounce;
	
	public basicblock(String blockData,String inputHash )
	{
		this.blockData = blockData;
		this.inputHash = inputHash;
		this.time = new Date().getTime();
		this.outputHash = hashCalculater();
	}
	
	public String hashCalculater() {
		String hashSHA256 = hashing.hashSha256(inputHash +Long.toString(time) + Integer.toString(uniNounce) + blockData );
		return hashSHA256;
	}
	
	//Increases nonce value until hash target is reached.
	public void blockMining(int profDif) {
		String target = hashing.proofOfWorkString(profDif); //Create a string with difficulty number of "0" 
		while(!outputHash.substring( 0, profDif).equals(target)) {
			uniNounce ++;
			outputHash = hashCalculater();
		}
		System.out.println("Output after block mining: " + outputHash);
	}
}
