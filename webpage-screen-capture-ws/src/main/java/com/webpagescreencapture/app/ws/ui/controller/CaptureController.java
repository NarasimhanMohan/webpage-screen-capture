package com.webpagescreencapture.app.ws.ui.controller;



import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.GeckoDriverInfo;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//import com.gargoylesoftware.htmlunit.javascript.host.html.Image;
import com.webpagescreencapture.app.ws.ui.model.request.UrlDetails;
import com.webpagescreencapture.app.ws.ui.model.response.UrlRest;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

@RestController
@RequestMapping("")
public class CaptureController {

	@PostMapping(consumes= {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
			produces= {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<byte[]> captureScreen(@RequestBody UrlDetails urlDetails) throws IOException
	{
		//Setting Property for chrome Driver
		System.setProperty("webdriver.chrome.driver","src/main/resources/chromedriver.exe");
		// Setting your Chrome options (Desired capabilities)
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless");  
        options.setHeadless(true);
        
		WebDriver driver = new ChromeDriver();
		//Getting URL
		driver.get(urlDetails.getUrl());
		//maximize the window
		driver.manage().window().maximize();
		//Take full screenshot using the AShot
		Screenshot screenshot=new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000)).takeScreenshot(driver);
		//Saving the screenshotted Imange in resource folder
		ImageIO.write(screenshot.getImage(),"JPEG",new File("src/main/resources/screenshot.jpg"));
		//Close the Driver
		driver.close();
		//Reading the screenshotted file
		var imgFile = new ClassPathResource("screenshot.jpg");
		//Converting them into byte array
        byte[] bytes = StreamUtils.copyToByteArray(imgFile.getInputStream());

        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(bytes);
		
        //Incase we need to sedn the requested URL in response we can use the UrlRest class
	}
}
