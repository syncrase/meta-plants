import { element, by, ElementFinder } from 'protractor';

export class EnsoleillementComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('perma-ensoleillement div table .btn-danger'));
  title = element.all(by.css('perma-ensoleillement div h2#page-heading span')).first();
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

export class EnsoleillementUpdatePage {
  pageTitle = element(by.id('perma-ensoleillement-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  orientationInput = element(by.id('field_orientation'));
  ensoleilementInput = element(by.id('field_ensoleilement'));

  planteSelect = element(by.id('field_plante'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setOrientationInput(orientation: string): Promise<void> {
    await this.orientationInput.sendKeys(orientation);
  }

  async getOrientationInput(): Promise<string> {
    return await this.orientationInput.getAttribute('value');
  }

  async setEnsoleilementInput(ensoleilement: string): Promise<void> {
    await this.ensoleilementInput.sendKeys(ensoleilement);
  }

  async getEnsoleilementInput(): Promise<string> {
    return await this.ensoleilementInput.getAttribute('value');
  }

  async planteSelectLastOption(): Promise<void> {
    await this.planteSelect.all(by.tagName('option')).last().click();
  }

  async planteSelectOption(option: string): Promise<void> {
    await this.planteSelect.sendKeys(option);
  }

  getPlanteSelect(): ElementFinder {
    return this.planteSelect;
  }

  async getPlanteSelectedOption(): Promise<string> {
    return await this.planteSelect.element(by.css('option:checked')).getText();
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

export class EnsoleillementDeleteDialog {
  private dialogTitle = element(by.id('perma-delete-ensoleillement-heading'));
  private confirmButton = element(by.id('perma-confirm-delete-ensoleillement'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
