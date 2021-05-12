import { Component, Inject } from 'vue-property-decorator';

import { mixins } from 'vue-class-component';
import JhiDataUtils from '@/shared/data/data-utils.service';

import { IAttachement } from '@/shared/model/attachement.model';
import AttachementService from './attachement.service';

@Component
export default class AttachementDetails extends mixins(JhiDataUtils) {
  @Inject('attachementService') private attachementService: () => AttachementService;
  public attachement: IAttachement = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.attachementId) {
        vm.retrieveAttachement(to.params.attachementId);
      }
    });
  }

  public retrieveAttachement(attachementId) {
    this.attachementService()
      .find(attachementId)
      .then(res => {
        this.attachement = res;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
