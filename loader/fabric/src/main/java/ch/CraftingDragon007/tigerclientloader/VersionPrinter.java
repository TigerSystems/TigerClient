package ch.CraftingDragon007.tigerclientloader;



public class VersionPrinter {

	static {
		final String first = "--::tc_loader=";
		final String last = "=tc_loader::--";
		System.out.println(first + LoaderMod.VERSION + last);
	}
	
	public static void __internal__() {}
	
	public static void recursivly() {
		VersionPrinter.recursivly();
	}
	
	
	
}
