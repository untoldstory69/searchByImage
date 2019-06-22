package searchByImage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class SearchByImage {
	
	public WebDriver driver;
	
	 String currentDir = System.getProperty("user.dir");
	 
  //test setup
  @BeforeTest
  public void beforeTest() {
	 //defining geco driver. Firefox is used
	    System.setProperty("webdriver.gecko.driver", currentDir + "\\geckodriver\\geckodriver.exe");
		driver = new FirefoxDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	  }

  //search by image
 @Test (priority = 1)	
  public void searchByImage() throws Exception {
	  driver.get("https://images.google.com/");
	  driver.findElement(By.className("S3Wjs")).click();
	  driver.findElement(By.linkText("Upload an image")).click();
	  driver.findElement(By.id("qbfile")).sendKeys(currentDir + "\\configuration\\dog.jpeg");
	  driver.findElement(By.className("iu-card-header")).click();
	  //open image
	  driver.findElement(By.id("Rk8vRSWIfuAzYM:")).click();
  }
 
 //take screenshot
 @Test (priority = 2)	
 public void takeScreenShot() throws Exception
 {
	 File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
	 FileUtils.copyFile(scrFile, new File(currentDir + "\\configuration\\lastVisitedPage.jpeg"));
 }
 
// download image and save it to configuration folder
 @Test (priority = 3)	
 public void downloadImage() throws Exception
 {
	 URL imageURL = null;
	  WebElement myElement = driver.findElement(By.xpath("/html/body/div[5]/div[3]/div[3]/div[2]/div/div[2]/div[2]/div/div/div/div/div[2]/div[2]/div[1]/div[2]/div[1]/div[2]/div[1]/a/img"));
	  String j = myElement.getAttribute("src");
     
             //generate url
             imageURL = new URL(j);
             
             //read url and retrieve image
             BufferedImage saveImage = ImageIO.read(imageURL);

             //downloading image in jpeg and saving in configuration folder
             ImageIO.write(saveImage, "jpeg", new File( currentDir +"\\configuration\\result.jpeg"));
 }
 
 // compare images
 @Test (priority = 4)
 public void compareImages() throws Exception
 {
	 String currentDir = System.getProperty("user.dir");
	//images to compare
     BufferedImage img1 = ImageIO.read(new File(currentDir + "\\configuration\\dog.jpeg"));
     BufferedImage img2 = ImageIO.read(new File(currentDir + "\\configuration\\result.jpeg"));
     
     double p = getDifferencePercent(img1, img2);
     System.out.println("The diff percent of the image is : " + p +"%");
     
     
 }

 //calculating percent
 @Test (priority = 5)
 private static double getDifferencePercent(BufferedImage img1, BufferedImage img2) {
     int width = img1.getWidth();
     int height = img1.getHeight();
     
     long diff = 0;
     for (int y = 0; y < height; y++) {
         for (int x = 0; x < width; x++) {
             diff += pixelDiff(img1.getRGB(x, y), img2.getRGB(x, y));
         }
     }
     long maxDiff = 3L * 255 * width * height;

     return 100.0 * diff / maxDiff;
 }

 //calculating pixel differences
 @Test (priority = 6)
 private static int pixelDiff(int rgb1, int rgb2) {
     int r1 = (rgb1 >> 16) & 0xff;
     int g1 = (rgb1 >>  8) & 0xff;
     int b1 =  rgb1        & 0xff;
     int r2 = (rgb2 >> 16) & 0xff;
     int g2 = (rgb2 >>  8) & 0xff;
     int b2 =  rgb2        & 0xff;
     return Math.abs(r1 - r2) + Math.abs(g1 - g2) + Math.abs(b1 - b2);
 }
 
//test tear down
  @AfterTest
  public void afterTest() {
	  driver.quit();
  }
  

}
