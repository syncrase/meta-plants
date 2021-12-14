import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { RacineComponentsPage, RacineDeleteDialog, RacineUpdatePage } from './racine.page-object';

const expect = chai.expect;

describe('Racine e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let racineComponentsPage: RacineComponentsPage;
  let racineUpdatePage: RacineUpdatePage;
  let racineDeleteDialog: RacineDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Racines', async () => {
    await navBarPage.goToEntity('racine');
    racineComponentsPage = new RacineComponentsPage();
    await browser.wait(ec.visibilityOf(racineComponentsPage.title), 5000);
    expect(await racineComponentsPage.getTitle()).to.eq('Racines');
    await browser.wait(ec.or(ec.visibilityOf(racineComponentsPage.entities), ec.visibilityOf(racineComponentsPage.noResult)), 1000);
  });

  it('should load create Racine page', async () => {
    await racineComponentsPage.clickOnCreateButton();
    racineUpdatePage = new RacineUpdatePage();
    expect(await racineUpdatePage.getPageTitle()).to.eq('Create or edit a Racine');
    await racineUpdatePage.cancel();
  });

  it('should create and save Racines', async () => {
    const nbButtonsBeforeCreate = await racineComponentsPage.countDeleteButtons();

    await racineComponentsPage.clickOnCreateButton();

    await promise.all([racineUpdatePage.setTypeInput('type')]);

    await racineUpdatePage.save();
    expect(await racineUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await racineComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Racine', async () => {
    const nbButtonsBeforeDelete = await racineComponentsPage.countDeleteButtons();
    await racineComponentsPage.clickOnLastDeleteButton();

    racineDeleteDialog = new RacineDeleteDialog();
    expect(await racineDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Racine?');
    await racineDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(racineComponentsPage.title), 5000);

    expect(await racineComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
