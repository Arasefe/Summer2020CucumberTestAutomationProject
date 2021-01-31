package com.vytrack.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import java.net.URL;

public class Driver {
    private static ThreadLocal<WebDriver> driverPool = new ThreadLocal<>();

    private Driver() { }

    public static WebDriver getDriver() {
        if (driverPool.get() == null) {
            //it will make that 2 threads cannot access this piece of code at the same time only 1 thread at the time
            synchronized (Driver.class) {
                String browser = ConfigurationReader.getProperty("browser");
            //jenkins command: test -Dcucumber.filter.tags="@smoke" -Dbrowser="chrome"
            //custom environment variables: -Dbrowser
            //-Dproperty  = then read in java System.getProperty("property")
            //if env variable was specified
                if (System.getProperty("browser") != null) {
            //then change browser type
            //regardless on value configuration.properties
                    System.out.println("Browser type was changed to: " + System.getProperty("browser"));
                    browser = System.getProperty("browser");
                }
                switch (browser) {
                    case "chrome":
                        WebDriverManager.chromedriver().setup();
                        driverPool.set(new ChromeDriver());
                        //driverPool.set(new ChromeDriver(new ChromeOptions().setHeadless(true)));
                        break;
                    case "firefox":
                        WebDriverManager.firefoxdriver().setup();
                        driverPool.set(new FirefoxDriver());
                        //driverPool.set(new FirefoxDriver(new FirefoxOptions().setHeadless(true)));
                        break;
                    case "remote-chrome":
                        try {
                            //ChromeOptions chromeOptions = new ChromeOptions();

                            DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
                            desiredCapabilities.setBrowserName("chrome");
                            URL gridUrl = new URL("http://18.205.160.8:4444/wd/hub");
                            driverPool.set(new RemoteWebDriver(gridUrl, desiredCapabilities));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case "remote-firefox":
                        try {

                            DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
                            desiredCapabilities.setBrowserName("firefox");
                            URL gridUrl = new URL("http://18.205.160.8:4444/wd/hub");
                            driverPool.set(new RemoteWebDriver(gridUrl, desiredCapabilities));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        throw new RuntimeException("No such a browser yet!");
                }
            }
        }
        return driverPool.get();
    }

    public static void closeDriver() {
        if (driverPool.get() != null) {
            driverPool.get().quit();
            driverPool.remove();
        }
    }
}
