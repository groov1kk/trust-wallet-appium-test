package com.github.groov1kk.screens.widgets;

import static com.github.groov1kk.utils.WebElementUtils.check;

import com.github.groov1kk.widgets.ExtendedWidget;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class PrivacyPolicies extends ExtendedWidget {

  @AndroidFindBy(id = "com.wallet.crypto.trustapp:id/acceptCheckBox")
  private WebElement checkboxAcceptPolicy;

  @AndroidFindBy(id = "com.wallet.crypto.trustapp:id/next")
  private WebElement buttonContinue;

  protected PrivacyPolicies(WebElement element) {
    super(element);
  }

  public void accept() {
    check(checkboxAcceptPolicy);
    buttonContinue.click();
  }
}
