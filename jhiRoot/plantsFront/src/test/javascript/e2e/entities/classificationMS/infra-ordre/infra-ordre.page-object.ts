import { element, by, ElementFinder } from 'protractor';

export class InfraOrdreComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('perma-infra-ordre div table .btn-danger'));
  title = element.all(by.css('perma-infra-ordre div h2#page-heading span')).first();
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

export class InfraOrdreUpdatePage {
  pageTitle = element(by.id('perma-infra-ordre-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  nomFrInput = element(by.id('field_nomFr'));
  nomLatinInput = element(by.id('field_nomLatin'));

  sousOrdreSelect = element(by.id('field_sousOrdre'));
  infraOrdreSelect = element(by.id('field_infraOrdre'));

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

  async sousOrdreSelectLastOption(): Promise<void> {
    await this.sousOrdreSelect.all(by.tagName('option')).last().click();
  }

  async sousOrdreSelectOption(option: string): Promise<void> {
    await this.sousOrdreSelect.sendKeys(option);
  }

  getSousOrdreSelect(): ElementFinder {
    return this.sousOrdreSelect;
  }

  async getSousOrdreSelectedOption(): Promise<string> {
    return await this.sousOrdreSelect.element(by.css('option:checked')).getText();
  }

  async infraOrdreSelectLastOption(): Promise<void> {
    await this.infraOrdreSelect.all(by.tagName('option')).last().click();
  }

  async infraOrdreSelectOption(option: string): Promise<void> {
    await this.infraOrdreSelect.sendKeys(option);
  }

  getInfraOrdreSelect(): ElementFinder {
    return this.infraOrdreSelect;
  }

  async getInfraOrdreSelectedOption(): Promise<string> {
    return await this.infraOrdreSelect.element(by.css('option:checked')).getText();
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

export class InfraOrdreDeleteDialog {
  private dialogTitle = element(by.id('perma-delete-infraOrdre-heading'));
  private confirmButton = element(by.id('perma-confirm-delete-infraOrdre'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
