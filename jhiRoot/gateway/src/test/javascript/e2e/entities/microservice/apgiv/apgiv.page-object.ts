import { element, by, ElementFinder } from 'protractor';

export class APGIVComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('gp-apgiv div table .btn-danger'));
  title = element.all(by.css('gp-apgiv div h2#page-heading span')).first();
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
    return this.title.getAttribute('gpTranslate');
  }
}

export class APGIVUpdatePage {
  pageTitle = element(by.id('gp-apgiv-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  ordreInput = element(by.id('field_ordre'));
  familleInput = element(by.id('field_famille'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('gpTranslate');
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
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

export class APGIVDeleteDialog {
  private dialogTitle = element(by.id('gp-delete-aPGIV-heading'));
  private confirmButton = element(by.id('gp-confirm-delete-aPGIV'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('gpTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
