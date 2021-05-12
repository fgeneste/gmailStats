import { Component, Inject } from 'vue-property-decorator';

import { mixins } from 'vue-class-component';
import JhiDataUtils from '@/shared/data/data-utils.service';

import MessageService from '@/entities/message/message.service';
import { IMessage } from '@/shared/model/message.model';

import { IAttachement, Attachement } from '@/shared/model/attachement.model';
import AttachementService from './attachement.service';

const validations: any = {
  attachement: {
    file: {},
  },
};

@Component({
  validations,
})
export default class AttachementUpdate extends mixins(JhiDataUtils) {
  @Inject('attachementService') private attachementService: () => AttachementService;
  public attachement: IAttachement = new Attachement();

  @Inject('messageService') private messageService: () => MessageService;

  public messages: IMessage[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.attachementId) {
        vm.retrieveAttachement(to.params.attachementId);
      }
      vm.initRelationships();
    });
  }

  created(): void {
    this.currentLanguage = this.$store.getters.currentLanguage;
    this.$store.watch(
      () => this.$store.getters.currentLanguage,
      () => {
        this.currentLanguage = this.$store.getters.currentLanguage;
      }
    );
  }

  public save(): void {
    this.isSaving = true;
    if (this.attachement.id) {
      this.attachementService()
        .update(this.attachement)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('gmailStatsApp.attachement.updated', { param: param.id });
          return this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        });
    } else {
      this.attachementService()
        .create(this.attachement)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('gmailStatsApp.attachement.created', { param: param.id });
          this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Success',
            variant: 'success',
            solid: true,
            autoHideDelay: 5000,
          });
        });
    }
  }

  public retrieveAttachement(attachementId): void {
    this.attachementService()
      .find(attachementId)
      .then(res => {
        this.attachement = res;
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.messageService()
      .retrieve()
      .then(res => {
        this.messages = res.data;
      });
  }
}
