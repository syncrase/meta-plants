import { browser, ExpectedConditions as ec /* , promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  AllelopathieComponentsPage,
  /* AllelopathieDeleteDialog, */
  AllelopathieUpdatePage,
} from './allelopathie.page-object';

const expect = chai.expect;

describe('Allelopathie e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let allelopathieComponentsPage: AllelopathieComponentsPage;
  let allelopathieUpdatePage: AllelopathieUpdatePage;
  /* let allelopathieDeleteDialog: AllelopathieDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Allelopathies', async () => {
    await navBarPage.goToEntity('allelopathie');
    allelopathieComponentsPage = new AllelopathieComponentsPage();
    await browser.wait(ec.visibilityOf(allelopathieComponentsPage.title), 5000);
    expect(await allelopathieComponentsPage.getTitle()).to.eq('Allelopathies');
    await browser.wait(
      ec.or(ec.visibilityOf(allelopathieComponentsPage.entities), ec.visibilityOf(allelopathieComponentsPage.noResult)),
      1000
    );
  });

  it('should load create Allelopathie page', async () => {
    await allelopathieComponentsPage.clickOnCreateButton();
    allelopathieUpdatePage = new AllelopathieUpdatePage();
    expect(await allelopathieUpdatePage.getPageTitle()).to.eq('Create or edit a Allelopathie');
    await allelopathieUpdatePage.cancel();
  });

  /* it('should create and save Allelopathies', async () => {
        const nbButtonsBeforeCreate = await allelopathieComponentsPage.countDeleteButtons();

        await allelopathieComponentsPage.clickOnCreateButton();

        await promise.all([
            allelopathieUpdatePage.setTypeInput('type'),
            allelopathieUpdatePage.setDescriptionInput('description'),
            allelopathieUpdatePage.setImpactInput('5'),
            allelopathieUpdatePage.cibleSelectLastOption(),
            allelopathieUpdatePage.origineSelectLastOption(),
        ]);

        await allelopathieUpdatePage.save();
        expect(await allelopathieUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await allelopathieComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last Allelopathie', async () => {
        const nbButtonsBeforeDelete = await allelopathieComponentsPage.countDeleteButtons();
        await allelopathieComponentsPage.clickOnLastDeleteButton();

        allelopathieDeleteDialog = new AllelopathieDeleteDialog();
        expect(await allelopathieDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Allelopathie?');
        await allelopathieDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(allelopathieComponentsPage.title), 5000);

        expect(await allelopathieComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
