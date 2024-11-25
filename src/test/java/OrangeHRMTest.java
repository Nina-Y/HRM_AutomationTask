import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class OrangeHRMTest extends TestBase {
    private final String baseUrl = "https://opensource-demo.orangehrmlive.com/";
    private final String username = "Admin";
    private final String password = "admin123";

    @Test(priority = 1)
    public void loginToOrangeHRM() {
        driver.get(baseUrl);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")));
        usernameField.sendKeys(username);
        driver.findElement(By.name("password")).sendKeys(password);

        driver.findElement(By.className("orangehrm-login-button")).click();

        WebElement dashboard = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='Dashboard']")));
        Assert.assertTrue(dashboard.isDisplayed(), "Login failed!");
    }

    @Test(priority = 2, dependsOnMethods = {"loginToOrangeHRM"})
    public void navigateToAssignLeave() {
        driver.findElement(By.xpath("//span[text()='Leave']")).click();

        WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement assignLeave = wait2.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[text()='Assign Leave']")));
        assignLeave.click();

        boolean isUrlCorrect = wait2.until(ExpectedConditions.urlToBe("https://opensource-demo.orangehrmlive.com/web/index.php/leave/assignLeave"));
        Assert.assertTrue(isUrlCorrect, "Navigation to Assign Leave page failed!");
    }

    @Test(priority = 3, dependsOnMethods = {"navigateToAssignLeave"})
    public void assignLeave() throws InterruptedException {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(40));
        WebElement employeeNameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@placeholder='Type for hints...']")));
        employeeNameInput.clear();
        employeeNameInput.sendKeys("F");

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@role='option' and text()='Searching....']")));

        // Debugging: Print out all the available name options according to the entered keyword 'J'
        List<WebElement> dropdownNameOptions = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                By.xpath("//div[@role='option']")));
        System.out.println("\nDropdown name options loaded:");
        for (WebElement option : dropdownNameOptions) {
            System.out.println("- option: " + option.getText());
        }

        // WebElement nameOption = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='option' and contains(text(),'James Butler')]"))); // TimeoutException (NoSuchElement)
        WebElement nameOption = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("(//div[@role='option'])[2]")));
        nameOption.click();
        Thread.sleep(2000); // 3 sec, to see the result

        // Debugging Name
        System.out.println("Clicked on dropdown name option: " + nameOption);

        // Leave type
        driver.findElement(By.xpath("//div[text()='-- Select --']")).click();

        // WebElement leaveTypeOption = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'US - Vacation')]"))); // TimeoutException (NoSuchElement)
        WebElement leaveTypeOption = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("(//div[@role='option'])[11]")));
        Thread.sleep(3000); // 3 sec, to see the result
        leaveTypeOption.click();

        // Debugging Leave Type
        System.out.println("Clicked on dropdown leave type option: " + leaveTypeOption);

        // From Date
        //WebElement fromDateField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='yyyy-dd-mm']"))); // as an option
        WebElement fromDateField = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("input[placeholder='yyyy-dd-mm']")));
        fromDateField.sendKeys("2020-10-19");

        Thread.sleep(3000); // 3 sec, to see the result

        // To Date
        // WebElement toDateField = driver.findElement(By.xpath("(//i[@class='oxd-icon bi-calendar oxd-date-input-icon'])[2]")).click(); - another option, second calendar icon
        // Second input with the placeholder "yyyy-dd-mm"
        WebElement toDateField = driver.findElement(By.xpath("(//input[@placeholder='yyyy-dd-mm'])[2]"));
        toDateField.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        toDateField.sendKeys(Keys.BACK_SPACE);
        toDateField.sendKeys("2020-10-23");
        Thread.sleep(3000); // 3 sec, to see the result

        // Partial Days - there's no option "None" as in the task, should I just choose "--Select--" from dropdown menu?; !Dropdown menu doesn't show; is it 2d or 3d occurrence of dropdown menu ("Duration"-2d?)
        /*WebElement partialDaysArrow = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("(//i[@class='oxd-icon bi-caret-down-fill oxd-select-text--arrow'])[3]")
        ));
        partialDaysArrow.click();
        WebElement partialDaysOption = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("(//div[@role='option'])[1]")
        ));
        partialDaysOption.click();*/

        // Comments
        driver.findElement(By.className("oxd-textarea--active")).sendKeys("- Not required -");
        Thread.sleep(3000); // 3 sec, to see the result

        // Click the "Assign" button
        driver.findElement(By.xpath("//button[@type='submit' and contains(@class, 'oxd-button--secondary')]")).click();
        Thread.sleep(3000); // 3 sec, to see the result

        // Confirm Leave Assignment with OK button and validate - even when Success msg is visible prints "failed" - ?!
        /*try {
            //WebElement confirmationDialog = wait.until(ExpectedConditions.visibilityOfElementLocated(
            WebElement okButton = wait.until(ExpectedConditions.elementToBeClickable(
                    // By.xpath("//button[@type='button' and contains(@class, 'oxd-button--secondary') and text()='Ok']"))); // "Confirmation failed"
                    // By.xpath("//button[text()='Ok' and contains(@class, 'oxd-button--secondary')]"))); // "Confirmation failed"
                    // By.cssSelector("button.oxd-button--secondary"))); // "Confirmation failed"
                    // By.xpath("//button[contains(@class,'oxd-button--secondary') and text()='Ok']"))); // "Confirmation failed"
                    By.xpath("//div[contains(@class, 'orangehrm-modal-footer')]//button[text()='Ok']")));



            okButton.click();
            System.out.println("Assign confirmed");
        } catch (Exception e) {
            System.out.println("Confirmation failed");
        }*/

        // Switch to the iframe (if the modal is inside one)
        driver.switchTo().frame("iframeNameOrID");

// Now interact with the "Ok" button inside the iframe
        WebElement okButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@class,'oxd-button--secondary') and text()='Ok']")));
        okButton.click();

// Switch back to the main page
        driver.switchTo().defaultContent();


        // Wait for the toast container to appear and validate Leave Assignment (option 2) // TimeoutException
        /*WebElement toastContainer = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("oxd-toaster_1")));
        String toastMessage = toastContainer.getText();
        System.out.println("Toast message: " + toastMessage);
        Assert.assertTrue(toastMessage.contains("Successfully Saved"), "Assignment was not successful!");*/

        // Validate Leave Assignment (option 3) // TimeoutException
        /*WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(text(),'Successfully Saved')]")));
        Assert.assertTrue(successMessage.isDisplayed(), "Leave assignment failed!");*/
    }

    @Test(priority = 4, dependsOnMethods = {"assignLeave"})
    public void logoutAndClose() {
        // Logout
        WebElement userDropdown = driver.findElement(By.cssSelector("p.oxd-userdropdown-name"));
        userDropdown.click();

        WebElement logoutLink = driver.findElement(By.xpath("//a[@class='oxd-userdropdown-link' and contains(text(), 'Logout')]"));
        logoutLink.click();

        // Validate logout success
        // WebElement loginPanel = driver.findElement(By.id("logInPanelHeading")); // ?
        //Assert.assertTrue(loginPanel.isDisplayed(), "Logout failed!");
    }
}

