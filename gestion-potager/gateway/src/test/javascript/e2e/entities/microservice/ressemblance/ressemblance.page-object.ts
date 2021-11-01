import { element, by, ElementFinder } from 'protractor';

export class RessemblanceComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('gp-ressemblance div table .btn-danger'));
  title = element.all(by.css('gp-ressemblance div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getAttribute('jhiTranslate');
  }
}

export class RessemblanceUpdatePage {
  pageTitle = element(by.id('gp-ressemblance-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  descriptionInput = element(by.id('field_description'));

  confusionSelect = element(by.id('field_confusion'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setDescriptionInput(description: string): Promise<void> {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput(): Promise<string> {
    return await this.descriptionInput.getAttribute('value');
  }

  async confusionSelectLastOption(): Promise<void> {
    await this.confusionSelect.all(by.tagName('option')).last().click();
  }

  async confusionSelectOption(option: string): Promise<void> {
    await this.confusionSelect.sendKeys(option);
  }

  getConfusionSelect(): ElementFinder {
    return this.confusionSelect;
  }

  async getConfusionSelectedOption(): Promise<string> {
    return await this.confusionSelect.element(by.css('option:checked')).getText();
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class RessemblanceDeleteDialog {
  private dialogTitle = element(by.id('gp-delete-ressemblance-heading'));
  private confirmButton = element(by.id('gp-confirm-delete-ressemblance'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
