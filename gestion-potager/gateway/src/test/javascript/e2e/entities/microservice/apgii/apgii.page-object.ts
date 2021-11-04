import { element, by, ElementFinder } from 'protractor';

export class APGIIComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('gp-apgii div table .btn-danger'));
  title = element.all(by.css('gp-apgii div h2#page-heading span')).first();
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

export class APGIIUpdatePage {
  pageTitle = element(by.id('gp-apgii-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  ordreInput = element(by.id('field_ordre'));
  familleInput = element(by.id('field_famille'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setOrdreInput(ordre: string): Promise<void> {
    await this.ordreInput.sendKeys(ordre);
  }

  async getOrdreInput(): Promise<string> {
    return await this.ordreInput.getAttribute('value');
  }

  async setFamilleInput(famille: string): Promise<void> {
    await this.familleInput.sendKeys(famille);
  }

  async getFamilleInput(): Promise<string> {
    return await this.familleInput.getAttribute('value');
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

export class APGIIDeleteDialog {
  private dialogTitle = element(by.id('gp-delete-aPGII-heading'));
  private confirmButton = element(by.id('gp-confirm-delete-aPGII'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}