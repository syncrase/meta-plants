import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { TemperatureComponentsPage, TemperatureDeleteDialog, TemperatureUpdatePage } from './temperature.page-object';

const expect = chai.expect;

describe('Temperature e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let temperatureComponentsPage: TemperatureComponentsPage;
  let temperatureUpdatePage: TemperatureUpdatePage;
  let temperatureDeleteDialog: TemperatureDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Temperatures', async () => {
    await navBarPage.goToEntity('temperature');
    temperatureComponentsPage = new TemperatureComponentsPage();
    await browser.wait(ec.visibilityOf(temperatureComponentsPage.title), 5000);
    expect(await temperatureComponentsPage.getTitle()).to.eq('Temperatures');
    await browser.wait(
      ec.or(ec.visibilityOf(temperatureComponentsPage.entities), ec.visibilityOf(temperatureComponentsPage.noResult)),
      1000
    );
  });

  it('should load create Temperature page', async () => {
    await temperatureComponentsPage.clickOnCreateButton();
    temperatureUpdatePage = new TemperatureUpdatePage();
    expect(await temperatureUpdatePage.getPageTitle()).to.eq('Create or edit a Temperature');
    await temperatureUpdatePage.cancel();
  });

  it('should create and save Temperatures', async () => {
    const nbButtonsBeforeCreate = await temperatureComponentsPage.countDeleteButtons();

    await temperatureComponentsPage.clickOnCreateButton();

    await promise.all([
      temperatureUpdatePage.setMinInput('5'),
      temperatureUpdatePage.setMaxInput('5'),
      temperatureUpdatePage.setDescriptionInput('description'),
      temperatureUpdatePage.setRusticiteInput('rusticite'),
    ]);

    await temperatureUpdatePage.save();
    expect(await temperatureUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await temperatureComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Temperature', async () => {
    const nbButtonsBeforeDelete = await temperatureComponentsPage.countDeleteButtons();
    await temperatureComponentsPage.clickOnLastDeleteButton();

    temperatureDeleteDialog = new TemperatureDeleteDialog();
    expect(await temperatureDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Temperature?');
    await temperatureDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(temperatureComponentsPage.title), 5000);

    expect(await temperatureComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
