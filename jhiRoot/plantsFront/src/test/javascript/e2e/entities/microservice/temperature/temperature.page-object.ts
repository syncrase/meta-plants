import { element, by, ElementFinder } from 'protractor';

export class TemperatureComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('perma-temperature div table .btn-danger'));
  title = element.all(by.css('perma-temperature div h2#page-heading span')).first();
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

export class TemperatureUpdatePage {
  pageTitle = element(by.id('perma-temperature-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  minInput = element(by.id('field_min'));
  maxInput = element(by.id('field_max'));
  descriptionInput = element(by.id('field_description'));
  rusticiteInput = element(by.id('field_rusticite'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setMinInput(min: string): Promise<void> {
    await this.minInput.sendKeys(min);
  }

  async getMinInput(): Promise<string> {
    return await this.minInput.getAttribute('value');
  }

  async setMaxInput(max: string): Promise<void> {
    await this.maxInput.sendKeys(max);
  }

  async getMaxInput(): Promise<string> {
    return await this.maxInput.getAttribute('value');
  }

  async setDescriptionInput(description: string): Promise<void> {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput(): Promise<string> {
    return await this.descriptionInput.getAttribute('value');
  }

  async setRusticiteInput(rusticite: string): Promise<void> {
    await this.rusticiteInput.sendKeys(rusticite);
  }

  async getRusticiteInput(): Promise<string> {
    return await this.rusticiteInput.getAttribute('value');
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

export class TemperatureDeleteDialog {
  private dialogTitle = element(by.id('perma-delete-temperature-heading'));
  private confirmButton = element(by.id('perma-confirm-delete-temperature'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
