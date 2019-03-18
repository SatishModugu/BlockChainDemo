
public class BlockChain {

	public static void main(String[] args) 
	{
		basicblock genesisBlock = new basicblock("Hi im the first block", "0");
		System.out.println("Hash for block 1 : " + genesisBlock.outputHash);

		basicblock secondBlock = new basicblock("Yo im the second block",genesisBlock.outputHash);
		System.out.println("Hash for block 2 : " + secondBlock.outputHash);

		basicblock thirdBlock = new basicblock("Hey im the third block",secondBlock.outputHash);
		System.out.println("Hash for block 3 : " + thirdBlock.outputHash);
	}

}
