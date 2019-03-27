import java.util.ArrayList;
import com.google.gson.GsonBuilder;

public class BlockChain {
	public static ArrayList<basicblock> chainOfBlocks = new ArrayList<basicblock>();
	public static int PoWdiffi = 6;

	public static void main(String[] args) 
	{
		System.out.println("Hii");
		insertBlock(new basicblock("First block or genisis block", "0"));
		System.out.println("Mined first block");

		insertBlock(new basicblock("second block in the chain",chainOfBlocks.get(chainOfBlocks.size()-1).outputHash));
		System.out.println("Mined Second Block");

		insertBlock(new basicblock("Third block in the chain",chainOfBlocks.get(chainOfBlocks.size()-1).outputHash));
		System.out.println("Mined third Block");
		System.out.println("\nBlockchain is Valid: " + isChainValid());
		
		String blockchainJson = hashing.JsonBuilder(chainOfBlocks);
		System.out.println("\nThe block chain: ");
		System.out.println(blockchainJson);
	}
	public static Boolean isChainValid() {
		basicblock currentBlock; 
		basicblock previousBlock;
		String hashTarget = new String(new char[PoWdiffi]).replace('\0', '0');

		//loop through blockchain to check hashes:
		for(int i=1; i < chainOfBlocks.size(); i++) {
			currentBlock = chainOfBlocks.get(i);
			previousBlock = chainOfBlocks.get(i-1);
			//compare registered hash and calculated hash:
			if(!currentBlock.outputHash.equals(currentBlock.hashCalculater()) ){
				System.out.println("Current Hashes not equal");			
				return false;
			}
			//compare previous hash and registered previous hash
			if(!previousBlock.outputHash.equals(currentBlock.inputHash) ) {
				System.out.println("Previous Hashes not equal");
				return false;
			}
			//check if hash is solved
			if(!currentBlock.outputHash.substring( 0, PoWdiffi).equals(hashTarget)) {
				System.out.println("This block hasn't been mined");
				return false;
			}

		}
		return true;
	}
	public static void insertBlock(basicblock newBlock) {
		System.out.println("Hello");
		newBlock.blockMining(PoWdiffi);
		System.out.println("HelloAgain");
		chainOfBlocks.add(newBlock);
		
	}
}
