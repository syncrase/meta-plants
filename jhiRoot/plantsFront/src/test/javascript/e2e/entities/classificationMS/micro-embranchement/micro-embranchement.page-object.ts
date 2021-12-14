import { element, by, ElementFinder } from 'protractor';

export class MicroEmbranchementComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('perma-micro-embranchement div table .btn-danger'));
  title = element.all(by.css('perma-micro-embranchement div h2#page-heading span')).first();
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
    return this.title.getText();
  }
}

export class MicroEmbranchementUpdatePage {
  pageTitle = element(by.id('perma-micro-embranchement-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  nomFrInput = element(by.id('field_nomFr'));
  nomLatinInput = element(by.id('field_nomLatin'));

  infraEmbranchementSelect = element(by.id('field_infraEmbranchement'));
  microEmbranchementSelect = element(by.id('field_microEmbranchement'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setNomFrInput(nomFr: string): Promise<void> {
    await this.nomFrInput.sendKeys(nomFr);
  }

  async getNomFrInput(): Promise<string> {
    return await this.nomFrInput.getAttribute('value');
  }

  async setNomLatinInput(nomLatin: string): Promise<void> {
    await this.nomLatinInput.sendKeys(nomLatin);
  }

  async getNomLatinInput(): Promise<string> {
    return await this.nomLatinInput.getAttribute('value');
  }

  async infraEmbranchementSelectLastOption(): Promise<void> {
    await this.infraEmbranchementSelect.all(by.tagName('option')).last().click();
  }

  async infraEmbranchementSelectOption(option: string): Promise<void> {
    await this.infraEmbranchementSelect.sendKeys(option);
  }

  getInfraEmbranchementSelect(): ElementFinder {
    return this.infraEmbranchementSelect;
  }

  async getInfraEmbranchementSelectedOption(): Promise<string> {
    return await this.infraEmbranchementSelect.element(by.css('option:checked')).getText();
  }

  async microEmbranchementSelectLastOption(): Promise<void> {
    await this.microEmbranchementSelect.all(by.tagName('option')).last().click();
  }

  async microEmbranchementSelectOption(option: string): Promise<void> {
    await this.microEmbranchementSelect.sendKeys(option);
  }

  getMicroEmbranchementSelect(): ElementFinder {
    return this.microEmbranchementSelect;
  }

  async getMicroEmbranchementSelectedOption(): Promise<string> {
    return await this.microEmbranchementSelect.element(by.css('option:checked')).getText();
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

export class MicroEmbranchementDeleteDialog {
  private dialogTitle = element(by.id('perma-delete-microEmbranchement-heading'));
  private confirmButton = element(by.id('perma-confirm-delete-microEmbranchement'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
