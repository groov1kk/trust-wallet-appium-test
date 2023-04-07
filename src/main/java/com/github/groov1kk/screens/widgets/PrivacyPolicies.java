package com.github.groov1kk.screens.widgets;

import com.github.groov1kk.widgets.Checkbox;
import com.github.groov1kk.widgets.CustomWidget;
import com.github.groov1kk.widgets.Text;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class PrivacyPolicies extends CustomWidget {

  @AndroidFindBy(id = "com.wallet.crypto.trustapp:id/acceptCheckBox")
  private Checkbox checkboxAcceptPolicy;

  @AndroidFindBy(id = "com.wallet.crypto.trustapp:id/next")
  private Text buttonContinue;

  protected PrivacyPolicies(WebElement element) {
    super(element);
  }

  public void accept() {
    checkboxAcceptPolicy.check();
    buttonContinue.click();
  }
}
