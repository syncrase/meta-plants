import { element, by, ElementFinder } from 'protractor';

export class SuperFamilleComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('perma-super-famille div table .btn-danger'));
  title = element.all(by.css('perma-super-famille div h2#page-heading span')).first();
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

export class SuperFamilleUpdatePage {
  pageTitle = element(by.id('perma-super-famille-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  nomFrInput = element(by.id('field_nomFr'));
  nomLatinInput = element(by.id('field_nomLatin'));

  microOrdreSelect = element(by.id('field_microOrdre'));
  superFamilleSelect = element(by.id('field_superFamille'));

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

  async microOrdreSelectLastOption(): Promise<void> {
    await this.microOrdreSelect.all(by.tagName('option')).last().click();
  }

  async microOrdreSelectOption(option: string): Promise<void> {
    await this.microOrdreSelect.sendKeys(option);
  }

  getMicroOrdreSelect(): ElementFinder {
    return this.microOrdreSelect;
  }

  async getMicroOrdreSelectedOption(): Promise<string> {
    return await this.microOrdreSelect.element(by.css('option:checked')).getText();
  }

  async superFamilleSelectLastOption(): Promise<void> {
    await this.superFamilleSelect.all(by.tagName('option')).last().click();
  }

  async superFamilleSelectOption(option: string): Promise<void> {
    await this.superFamilleSelect.sendKeys(option);
  }

  getSuperFamilleSelect(): ElementFinder {
    return this.superFamilleSelect;
  }

  async getSuperFamilleSelectedOption(): Promise<string> {
    return await this.superFamilleSelect.element(by.css('option:checked')).getText();
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

export class SuperFamilleDeleteDialog {
  private dialogTitle = element(by.id('perma-delete-superFamille-heading'));
  private confirmButton = element(by.id('perma-confirm-delete-superFamille'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
