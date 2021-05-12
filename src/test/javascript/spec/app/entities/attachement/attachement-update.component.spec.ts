/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import * as config from '@/shared/config/config';
import AttachementUpdateComponent from '@/entities/attachement/attachement-update.vue';
import AttachementClass from '@/entities/attachement/attachement-update.component';
import AttachementService from '@/entities/attachement/attachement.service';

import MessageService from '@/entities/message/message.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.component('font-awesome-icon', {});
localVue.component('b-input-group', {});
localVue.component('b-input-group-prepend', {});
localVue.component('b-form-datepicker', {});
localVue.component('b-form-input', {});

describe('Component Tests', () => {
  describe('Attachement Management Update Component', () => {
    let wrapper: Wrapper<AttachementClass>;
    let comp: AttachementClass;
    let attachementServiceStub: SinonStubbedInstance<AttachementService>;

    beforeEach(() => {
      attachementServiceStub = sinon.createStubInstance<AttachementService>(AttachementService);

      wrapper = shallowMount<AttachementClass>(AttachementUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          attachementService: () => attachementServiceStub,

          messageService: () => new MessageService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.attachement = entity;
        attachementServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(attachementServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.attachement = entity;
        attachementServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(attachementServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundAttachement = { id: 123 };
        attachementServiceStub.find.resolves(foundAttachement);
        attachementServiceStub.retrieve.resolves([foundAttachement]);

        // WHEN
        comp.beforeRouteEnter({ params: { attachementId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.attachement).toBe(foundAttachement);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        comp.previousState();
        await comp.$nextTick();

        expect(comp.$router.currentRoute.fullPath).toContain('/');
      });
    });
  });
});
