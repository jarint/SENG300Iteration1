package com.diy.software;

import com.diy.hardware.BarcodedProduct;
import com.diy.hardware.external.ProductDatabases;
import com.diy.simulation.Customer;
import com.jimmyselectronics.necchi.Barcode;
import com.jimmyselectronics.necchi.BarcodedItem;
import com.jimmyselectronics.necchi.BarcodeScanner;
import com.jimmyselectronics.necchi.BarcodeScannerListener;
import com.jimmyselectronics.necchi.Numeral;

import ca.ucalgary.seng300.simulation.InvalidArgumentSimulationException.java;
import ca.ucalgary.seng300.simulation.NullPointerSimulationException.java;

/*
 * @author Jarin Thundathil
 */
public class addItemWithScanner {
	
	private Barcode barcode;
	private BarcodedProduct product;
	private BarcodedItem item;
	private BarcodeScanner scanner;
	private BarcodeScannerLister listener;
	private ProductDatabases pDB;
	
	private int Total = 0;
	
	public addItemWithScanner() {
		//empty constructor
	}
	
	/*
	 * Primary scanning method. calls the scanner.scan() method inside 
	 * @param scanner: A BarcodeScanner object
	 * @param item: A BarcodedItem object
	 */
	public void scanItem(BarcodeScanner scanner,BarcodedItem item, ProductDatabases pDB) {
		if (checkDBforItem(pDB, item)) {
			if(scanner.scan(item)) {
				scanner.notifyBarcodeScanned(item.getBarcode());
				displayScannedItem(item.getBarcode());
				updateTotal(pDB, item);
			} else {
				System.out.println("Barcode not scanned. Try again.");
			}
		} else {
			System.out.println("Product not found.");
		}
	}
	
	/*
	 * Method that displays the scanned item on the display
	 */
	public void displayScannedItem(Barcode barcode) {
		System.out.println("Scanned item: " + barcode.toString());
	}
	
	/*
	 * Accesses the barcode hashmap to return the price from the barcode class
	 */
	public void updateTotal(ProductDatabases pDB, BarcodedItem item) {
		Total += pDB.BARCODED_PRODUCT_DATABASE.get(item.getBarcode()).getPrice();
	}
	
	/*
	 * This method ensures that a scanned item is in the store database prior to initializing
	 */
	public boolean checkDBforItem(ProductDatabases pDB,BarcodedItem item) {
		if (pDB.BARCODED_PRODUCT_DATABASE.containsValue(item.getBarcode())) {
			return true;
		} else {
			return false;
		}
	}
	
}
