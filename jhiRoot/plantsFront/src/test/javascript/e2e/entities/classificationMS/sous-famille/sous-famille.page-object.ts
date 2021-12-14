import { element, by, ElementFinder } from 'protractor';

export class SousFamilleComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('perma-sous-famille div table .btn-danger'));
  title = element.all(by.css('perma-sous-famille div h2#page-heading span')).first();
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

export class SousFamilleUpdatePage {
  pageTitle = element(by.id('perma-sous-famille-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  nomFrInput = element(by.id('field_nomFr'));
  nomLatinInput = element(by.id('field_nomLatin'));

  familleSelect = element(by.id('field_famille'));
  sousFamilleSelect = element(by.id('field_sousFamille'));

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

  async familleSelectLastOption(): Promise<void> {
    await this.familleSelect.all(by.tagName('option')).last().click();
  }

  async familleSelectOption(option: string): Promise<void> {
    await this.familleSelect.sendKeys(option);
  }

  getFamilleSelect(): ElementFinder {
    return this.familleSelect;
  }

  async getFamilleSelectedOption(): Promise<string> {
    return await this.familleSelect.element(by.css('option:checked')).getText();
  }

  async sousFamilleSelectLastOption(): Promise<void> {
    await this.sousFamilleSelect.all(by.tagName('option')).last().click();
  }

  async sousFamilleSelectOption(option: string): Promise<void> {
    await this.sousFamilleSelect.sendKeys(option);
  }

  getSousFamilleSelect(): ElementFinder {
    return this.sousFamilleSelect;
  }

  async getSousFamilleSelectedOption(): Promise<string> {
    return await this.sousFamilleSelect.element(by.css('option:checked')).getText();
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

export class SousFamilleDeleteDialog {
  private dialogTitle = element(by.id('perma-delete-sousFamille-heading'));
  private confirmButton = element(by.id('perma-confirm-delete-sousFamille'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
