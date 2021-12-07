import { element, by, ElementFinder } from 'protractor';

export class CycleDeVieComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('perma-cycle-de-vie div table .btn-danger'));
  title = element.all(by.css('perma-cycle-de-vie div h2#page-heading span')).first();
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

export class CycleDeVieUpdatePage {
  pageTitle = element(by.id('perma-cycle-de-vie-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));

  semisSelect = element(by.id('field_semis'));
  apparitionFeuillesSelect = element(by.id('field_apparitionFeuilles'));
  floraisonSelect = element(by.id('field_floraison'));
  recolteSelect = element(by.id('field_recolte'));
  croissanceSelect = element(by.id('field_croissance'));
  maturiteSelect = element(by.id('field_maturite'));
  plantationSelect = element(by.id('field_plantation'));
  rempotageSelect = element(by.id('field_rempotage'));
  reproductionSelect = element(by.id('field_reproduction'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async semisSelectLastOption(): Promise<void> {
    await this.semisSelect.all(by.tagName('option')).last().click();
  }

  async semisSelectOption(option: string): Promise<void> {
    await this.semisSelect.sendKeys(option);
  }

  getSemisSelect(): ElementFinder {
    return this.semisSelect;
  }

  async getSemisSelectedOption(): Promise<string> {
    return await this.semisSelect.element(by.css('option:checked')).getText();
  }

  async apparitionFeuillesSelectLastOption(): Promise<void> {
    await this.apparitionFeuillesSelect.all(by.tagName('option')).last().click();
  }

  async apparitionFeuillesSelectOption(option: string): Promise<void> {
    await this.apparitionFeuillesSelect.sendKeys(option);
  }

  getApparitionFeuillesSelect(): ElementFinder {
    return this.apparitionFeuillesSelect;
  }

  async getApparitionFeuillesSelectedOption(): Promise<string> {
    return await this.apparitionFeuillesSelect.element(by.css('option:checked')).getText();
  }

  async floraisonSelectLastOption(): Promise<void> {
    await this.floraisonSelect.all(by.tagName('option')).last().click();
  }

  async floraisonSelectOption(option: string): Promise<void> {
    await this.floraisonSelect.sendKeys(option);
  }

  getFloraisonSelect(): ElementFinder {
    return this.floraisonSelect;
  }

  async getFloraisonSelectedOption(): Promise<string> {
    return await this.floraisonSelect.element(by.css('option:checked')).getText();
  }

  async recolteSelectLastOption(): Promise<void> {
    await this.recolteSelect.all(by.tagName('option')).last().click();
  }

  async recolteSelectOption(option: string): Promise<void> {
    await this.recolteSelect.sendKeys(option);
  }

  getRecolteSelect(): ElementFinder {
    return this.recolteSelect;
  }

  async getRecolteSelectedOption(): Promise<string> {
    return await this.recolteSelect.element(by.css('option:checked')).getText();
  }

  async croissanceSelectLastOption(): Promise<void> {
    await this.croissanceSelect.all(by.tagName('option')).last().click();
  }

  async croissanceSelectOption(option: string): Promise<void> {
    await this.croissanceSelect.sendKeys(option);
  }

  getCroissanceSelect(): ElementFinder {
    return this.croissanceSelect;
  }

  async getCroissanceSelectedOption(): Promise<string> {
    return await this.croissanceSelect.element(by.css('option:checked')).getText();
  }

  async maturiteSelectLastOption(): Promise<void> {
    await this.maturiteSelect.all(by.tagName('option')).last().click();
  }

  async maturiteSelectOption(option: string): Promise<void> {
    await this.maturiteSelect.sendKeys(option);
  }

  getMaturiteSelect(): ElementFinder {
    return this.maturiteSelect;
  }

  async getMaturiteSelectedOption(): Promise<string> {
    return await this.maturiteSelect.element(by.css('option:checked')).getText();
  }

  async plantationSelectLastOption(): Promise<void> {
    await this.plantationSelect.all(by.tagName('option')).last().click();
  }

  async plantationSelectOption(option: string): Promise<void> {
    await this.plantationSelect.sendKeys(option);
  }

  getPlantationSelect(): ElementFinder {
    return this.plantationSelect;
  }

  async getPlantationSelectedOption(): Promise<string> {
    return await this.plantationSelect.element(by.css('option:checked')).getText();
  }

  async rempotageSelectLastOption(): Promise<void> {
    await this.rempotageSelect.all(by.tagName('option')).last().click();
  }

  async rempotageSelectOption(option: string): Promise<void> {
    await this.rempotageSelect.sendKeys(option);
  }

  getRempotageSelect(): ElementFinder {
    return this.rempotageSelect;
  }

  async getRempotageSelectedOption(): Promise<string> {
    return await this.rempotageSelect.element(by.css('option:checked')).getText();
  }

  async reproductionSelectLastOption(): Promise<void> {
    await this.reproductionSelect.all(by.tagName('option')).last().click();
  }

  async reproductionSelectOption(option: string): Promise<void> {
    await this.reproductionSelect.sendKeys(option);
  }

  getReproductionSelect(): ElementFinder {
    return this.reproductionSelect;
  }

  async getReproductionSelectedOption(): Promise<string> {
    return await this.reproductionSelect.element(by.css('option:checked')).getText();
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

export class CycleDeVieDeleteDialog {
  private dialogTitle = element(by.id('perma-delete-cycleDeVie-heading'));
  private confirmButton = element(by.id('perma-confirm-delete-cycleDeVie'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
