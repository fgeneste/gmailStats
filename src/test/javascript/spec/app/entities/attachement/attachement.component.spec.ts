/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import AttachementComponent from '@/entities/attachement/attachement.vue';
import AttachementClass from '@/entities/attachement/attachement.component';
import AttachementService from '@/entities/attachement/attachement.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('b-badge', {});
localVue.directive('b-modal', {});
localVue.component('b-button', {});
localVue.component('router-link', {});

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  describe('Attachement Management Component', () => {
    let wrapper: Wrapper<AttachementClass>;
    let comp: AttachementClass;
    let attachementServiceStub: SinonStubbedInstance<AttachementService>;

    beforeEach(() => {
      attachementServiceStub = sinon.createStubInstance<AttachementService>(AttachementService);
      attachementServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<AttachementClass>(AttachementComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          attachementService: () => attachementServiceStub,
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      attachementServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllAttachements();
      await comp.$nextTick();

      // THEN
      expect(attachementServiceStub.retrieve.called).toBeTruthy();
      expect(comp.attachements[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      attachementServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      comp.removeAttachement();
      await comp.$nextTick();

      // THEN
      expect(attachementServiceStub.delete.called).toBeTruthy();
      expect(attachementServiceStub.retrieve.callCount).toEqual(1);
    });
  });
});
