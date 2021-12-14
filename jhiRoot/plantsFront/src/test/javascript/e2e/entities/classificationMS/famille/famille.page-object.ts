import { element, by, ElementFinder } from 'protractor';

export class FamilleComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('perma-famille div table .btn-danger'));
  title = element.all(by.css('perma-famille div h2#page-heading span')).first();
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

export class FamilleUpdatePage {
  pageTitle = element(by.id('perma-famille-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  nomFrInput = element(by.id('field_nomFr'));
  nomLatinInput = element(by.id('field_nomLatin'));

  superFamilleSelect = element(by.id('field_superFamille'));
  familleSelect = element(by.id('field_famille'));

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

export class FamilleDeleteDialog {
  private dialogTitle = element(by.id('perma-delete-famille-heading'));
  private confirmButton = element(by.id('perma-confirm-delete-famille'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
