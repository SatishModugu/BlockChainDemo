package basic;
import java.util.ArrayList;
import java.util.Date;

import transferApp.TransferHandler;
public class basicblock {

	public 	String outputHash;
	public  String inputHash;
	private String blockData;
	public ArrayList<TransferHandler> transfers = new ArrayList<TransferHandler>();
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
	//Add transactions to this block
	public boolean addTransaction(TransferHandler transfer) {
		//process transaction and check if valid, unless block is genesis block then ignore.
		if(transfer == null) return false;		
		if((inputHash != "0")) {
			if((transfer.processTransaction() != true)) {
				System.out.println("Problem in processtransaction. Transaction Denined.");
				return false;
			}
		}
		transfers.add(transfer);
		System.out.println("Transaction is added to the Block");
		return true;
	}
}
