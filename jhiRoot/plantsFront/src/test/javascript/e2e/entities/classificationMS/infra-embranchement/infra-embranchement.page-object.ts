import { element, by, ElementFinder } from 'protractor';

export class InfraEmbranchementComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('perma-infra-embranchement div table .btn-danger'));
  title = element.all(by.css('perma-infra-embranchement div h2#page-heading span')).first();
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

export class InfraEmbranchementUpdatePage {
  pageTitle = element(by.id('perma-infra-embranchement-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  nomFrInput = element(by.id('field_nomFr'));
  nomLatinInput = element(by.id('field_nomLatin'));

  sousDivisionSelect = element(by.id('field_sousDivision'));
  infraEmbranchementSelect = element(by.id('field_infraEmbranchement'));

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

  async sousDivisionSelectLastOption(): Promise<void> {
    await this.sousDivisionSelect.all(by.tagName('option')).last().click();
  }

  async sousDivisionSelectOption(option: string): Promise<void> {
    await this.sousDivisionSelect.sendKeys(option);
  }

  getSousDivisionSelect(): ElementFinder {
    return this.sousDivisionSelect;
  }

  async getSousDivisionSelectedOption(): Promise<string> {
    return await this.sousDivisionSelect.element(by.css('option:checked')).getText();
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

export class InfraEmbranchementDeleteDialog {
  private dialogTitle = element(by.id('perma-delete-infraEmbranchement-heading'));
  private confirmButton = element(by.id('perma-confirm-delete-infraEmbranchement'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
