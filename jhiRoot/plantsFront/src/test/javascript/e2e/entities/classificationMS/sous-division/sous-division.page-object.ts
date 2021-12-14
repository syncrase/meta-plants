import { element, by, ElementFinder } from 'protractor';

export class SousDivisionComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('perma-sous-division div table .btn-danger'));
  title = element.all(by.css('perma-sous-division div h2#page-heading span')).first();
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

export class SousDivisionUpdatePage {
  pageTitle = element(by.id('perma-sous-division-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  nomFrInput = element(by.id('field_nomFr'));
  nomLatinInput = element(by.id('field_nomLatin'));

  divisionSelect = element(by.id('field_division'));
  sousDivisionSelect = element(by.id('field_sousDivision'));

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

  async divisionSelectLastOption(): Promise<void> {
    await this.divisionSelect.all(by.tagName('option')).last().click();
  }

  async divisionSelectOption(option: string): Promise<void> {
    await this.divisionSelect.sendKeys(option);
  }

  getDivisionSelect(): ElementFinder {
    return this.divisionSelect;
  }

  async getDivisionSelectedOption(): Promise<string> {
    return await this.divisionSelect.element(by.css('option:checked')).getText();
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

export class SousDivisionDeleteDialog {
  private dialogTitle = element(by.id('perma-delete-sousDivision-heading'));
  private confirmButton = element(by.id('perma-confirm-delete-sousDivision'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
